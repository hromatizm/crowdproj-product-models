package com.product.model

fun main() {
    val environment = loadConfigAsMap()
    val config = AppKafkaConfig(env = environment)
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()))
    consumer.start()
}
