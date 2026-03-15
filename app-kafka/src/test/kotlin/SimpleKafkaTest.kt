package com.product.model

import io.kotest.core.spec.style.FunSpec
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.testcontainers.kafka.KafkaContainer
import java.time.Duration
import java.time.Instant
import java.util.*

class SimpleKafkaTest : FunSpec({

    val kafka = KafkaContainer("apache/kafka-native:3.8.0")
    val topicName = "producer-topic"

    beforeSpec {
        kafka.start()
    }

    afterSpec {
        kafka.stop()
    }

    test("producerTest") {
        val props = Properties().apply {
            put("bootstrap.servers", kafka.bootstrapServers) // динамический порт от Testcontainers
            put("acks", "all")
            put("retries", 0)
            put("batch.size", 16384)
            put("buffer.memory", 33554432)
            put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        }

        KafkaProducer<String, String>(props).use { producer ->
            (0..<10).forEach {
                producer.send(ProducerRecord(topicName, "key#$it", "Message number $it"))
            }
            println("Message sent successfully")
        }
    }

    test("consumerTest") {
        val props = Properties().apply {
            put("bootstrap.servers", kafka.bootstrapServers)
            put("group.id", "test")
            put("enable.auto.commit", "true")
            put("auto.commit.interval.ms", "1000")
            put("session.timeout.ms", "30000")
            put("auto.offset.reset", "earliest")
            put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        }

        KafkaConsumer<String, String>(props).use { consumer ->
            consumer.subscribe(listOf(topicName))
            val timeWithTimeout = Instant.now() + Duration.ofSeconds(5)

            while (timeWithTimeout > Instant.now()) {
                val records = consumer.poll(Duration.ofMillis(100))
                records.forEach { record ->
                    println("topic=${record.topic()}, offset=${record.offset()}, key=${record.key()}, value=${record.value()}")
                }
            }
        }
    }
})
