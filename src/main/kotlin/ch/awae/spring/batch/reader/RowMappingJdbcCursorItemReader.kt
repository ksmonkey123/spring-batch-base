package ch.awae.spring.batch.reader

import org.springframework.batch.item.database.JdbcCursorItemReader
import java.sql.ResultSet
import javax.sql.DataSource

open class RowMappingJdbcCursorItemReader<T : Any>(
    dataSource: DataSource,
    sql: String,
    rowMapping: (ResultSet, Int) -> T?
) : JdbcCursorItemReader<T>() {

    init {
        this.dataSource = dataSource
        this.sql = sql
        this.setRowMapper(rowMapping)
    }

}