package ch.awae.spring.batch

import org.springframework.boot.*
import kotlin.system.*

/**
 * run batch with custom initializer.
 *
 * before the initializer is executed, the [SpringApplication.webApplicationType] set to [WebApplicationType.NONE]
 */
fun <T> runBatch(clazz: Class<T>, vararg args: String, initializer: SpringApplication.() -> Unit): Nothing {
    val app = SpringApplication(clazz)
    app.webApplicationType = WebApplicationType.NONE
    initializer(app)
    runBatch(app, *args)
}

/**
 * run batch with [SpringApplication.webApplicationType] set to [WebApplicationType.NONE]
 */
fun <T> runBatch(clazz: Class<T>, vararg args: String): Nothing {
    runBatch(clazz, *args) {
        // no-op initializer
    }
}

/**
 * run batch from pre-initialized [SpringApplication]
 */
fun runBatch(app: SpringApplication, vararg args: String): Nothing {
    val ctx = app.run(*args)
    val exc = SpringApplication.exit(ctx)
    exitProcess(exc)
}
