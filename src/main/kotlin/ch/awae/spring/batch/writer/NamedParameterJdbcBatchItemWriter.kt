package ch.awae.spring.batch.writer

import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import javax.sql.DataSource

open class NamedParameterJdbcBatchItemWriter<T : Any>(
    dataSource: DataSource,
    sql: String,
    parameterMapping: (T) -> Map<String, Any?>
) : JdbcBatchItemWriter<T>() {

    init {
        this.setDataSource(dataSource)
        this.setSql(sql)
        this.setItemSqlParameterSourceProvider { item -> MapSqlParameterSource(parameterMapping(item)) }
    }

}