package ch.awae.spring.batch

import org.springframework.boot.autoconfigure.batch.BatchDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class BatchDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @BatchDataSource
    @ConfigurationProperties(prefix = "batchmeta.datasource")
    fun batchDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    @ConditionalOnProperty(
        value = ["spring.datasource.disabled"],
        havingValue = "false",
        matchIfMissing = true
    )
    fun dataSource(properties: DataSourceProperties): DataSource {
        return properties.initializeDataSourceBuilder().build()
    }

    @Bean
    @BatchDataSource
    fun batchMetaDataSource(@BatchDataSource properties: DataSourceProperties): DataSource {
        return properties.initializeDataSourceBuilder().build()
    }

}