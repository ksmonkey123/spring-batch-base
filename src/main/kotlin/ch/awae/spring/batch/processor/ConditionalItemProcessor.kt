package ch.awae.spring.batch.processor

import org.springframework.batch.item.ItemProcessor

class ConditionalItemProcessor<T : Any>(
    private val backer: ItemProcessor<T, T>,
    private val condition: (T) -> Boolean
) : ItemProcessor<T, T> {
    override fun process(item: T): T? = if (condition(item)) backer.process(item) else item

}