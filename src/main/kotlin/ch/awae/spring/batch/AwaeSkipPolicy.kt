package ch.awae.spring.batch

import org.springframework.batch.core.step.skip.SkipPolicy
import java.util.logging.Level
import java.util.logging.Logger

class AwaeSkipPolicy(private val skipLimit: Int = -1) : SkipPolicy {

    private val logger = Logger.getLogger(javaClass.name)
    override fun shouldSkip(t: Throwable, skipCount: Long): Boolean {
        return (t is ItemSkipException) && (skipLimit < 0 || skipCount < skipLimit)
    }
}