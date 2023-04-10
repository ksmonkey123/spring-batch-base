package ch.awae.spring.batch

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import java.util.logging.Logger

class FailJobWithSkipsListener : JobExecutionListener {

    val logger = Logger.getLogger(javaClass.name)
    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.stepExecutions.sumOf { it.skipCount } > 0) {
            // we have at least 1 skip event in the job -> fail it
            logger.info("marking job as [FAILED] due to skips")
            jobExecution.upgradeStatus(BatchStatus.FAILED)
        }
    }

}