package ch.awae.spring.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.configuration.BatchConfigurationException
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.batch.JobExecutionExitCodeGenerator
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import java.util.*
import javax.sql.DataSource

@Import(BatchDataSourceConfiguration::class)
abstract class AwaeBatchConfigurationBase(private val permitRerun: Boolean = true) : DefaultBatchConfiguration() {

    @Bean
    fun jobRunner(jobLauncher: JobLauncher, jobExplorer: JobExplorer, jobRepository: JobRepository) =
        JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)

    /**
     * inject a UUID as a batch parameter ("uuid") to enable arbitrary re-runs
     */
    override fun jobLauncher(): JobLauncher {
        if (permitRerun) {
            return try {
                val launcher = object : TaskExecutorJobLauncher() {
                    override fun run(job: Job, jobParameters: JobParameters): JobExecution {

                        val map = jobParameters.parameters.toMutableMap()
                        map["__run_uuid"] = JobParameter(UUID.randomUUID().toString(), String::class.java)

                        return super.run(job, JobParameters(map))
                    }
                }
                launcher.setJobRepository(jobRepository())
                launcher.setTaskExecutor(taskExecutor)
                launcher.afterPropertiesSet()
                launcher
            } catch (e: Exception) {
                throw BatchConfigurationException("Unable to configure the default job launcher", e)
            }
        } else {
            return super.jobLauncher()
        }
    }

    @Bean
    fun exitCodeListener() = JobExecutionExitCodeGenerator()

    override fun getDataSource(): DataSource {
        return applicationContext.getBean("batchMetaDataSource", DataSource::class.java)
    }

}