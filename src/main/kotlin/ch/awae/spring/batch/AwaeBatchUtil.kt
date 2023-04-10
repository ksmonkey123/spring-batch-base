package ch.awae.spring.batch

import org.springframework.boot.SpringApplication
import kotlin.system.exitProcess

object AwaeBatchUtil {

    fun <T> runBatch(clazz: Class<T>, vararg args: String, initializer: SpringApplication.() -> Unit): Nothing {
        val app = SpringApplication(clazz)
        initializer(app)
        runBatch(app, *args)
    }

    fun <T> runBatch(clazz: Class<T>, vararg args: String): Nothing {
        val app = SpringApplication(clazz)
        runBatch(app, *args)
    }

    fun runBatch(app: SpringApplication, vararg args: String): Nothing {
        val ctx = app.run(*args)
        val exc = SpringApplication.exit(ctx)
        exitProcess(exc)
    }

}