package ch.awae.spring.batch

import org.springframework.batch.core.step.skip.*

class AwaeSkipPolicy(private val skipLimit: Int = -1) : SkipPolicy {

    override fun shouldSkip(t: Throwable, skipCount: Long): Boolean {
        return (t is ItemSkipException) && (skipLimit < 0 || skipCount < skipLimit)
    }

}