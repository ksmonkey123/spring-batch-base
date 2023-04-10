package ch.awae.spring.batch.processor

import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.support.CompositeItemProcessor

class ProcessorPipeline<I : Any, O : Any> private constructor(private vararg val delegates: ItemProcessor<*, *>) {
    fun <T : Any> next(itemProcessor: ItemProcessor<O, T>): ProcessorPipeline<I, T> =
        ProcessorPipeline(*delegates, itemProcessor)

    fun conditional(itemProcessor: ItemProcessor<O, O>, condition: (O) -> Boolean): ProcessorPipeline<I, O> =
        ProcessorPipeline(*delegates, ConditionalItemProcessor(itemProcessor, condition))

    fun filter(test: (O) -> Boolean): ProcessorPipeline<I, O> = next { if (test(it)) it else null }

    fun <T : Any> map(mapping: (O) -> T): ProcessorPipeline<I, T> = next { mapping(it) }

    fun build(): ItemProcessor<I, O> = CompositeItemProcessor<I, O>(*delegates).also { it.afterPropertiesSet() }

    companion object {
        fun <I : Any, O : Any> start(itemProcessor: ItemProcessor<I, O>) = ProcessorPipeline<I, O>(itemProcessor)
    }

}