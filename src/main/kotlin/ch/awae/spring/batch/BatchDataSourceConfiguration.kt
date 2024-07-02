package ch.awae.spring.batch

import com.zaxxer.hikari.HikariDataSource
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
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    fun dataSource(properties: DataSourceProperties): DataSource {
        return properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    @BatchDataSource
    @ConfigurationProperties(prefix = "batchmeta.datasource.hikari")
    fun batchMetaDataSource(@BatchDataSource properties: DataSourceProperties): DataSource {
        return properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

}