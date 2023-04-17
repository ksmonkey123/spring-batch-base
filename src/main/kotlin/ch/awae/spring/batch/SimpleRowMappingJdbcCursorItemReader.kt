package ch.awae.spring.batch

import org.springframework.batch.item.database.JdbcCursorItemReader
import java.sql.ResultSet
import javax.sql.DataSource

open class SimpleRowMappingJdbcCursorItemReader<T : Any>(
    dataSource: DataSource,
    sql: String,
    rowMapping: (ResultSet) -> T?
) : JdbcCursorItemReader<T>() {

    init {
        this.dataSource = dataSource
        this.sql = sql
        this.setRowMapper { rs, _ -> rowMapping(rs) }
    }

}