package com.product.model

import com.product.model.api.v1.models.PmCreateObject
import com.product.model.api.v1.models.PmCreateRequest
import com.product.model.api.v1.models.PmCreateResponse
import com.product.model.api.v1.models.PmDebug
import com.product.model.api.v1.models.PmRequestDebugMode
import com.product.model.api.v1.models.PmRequestDebugStubs
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

private const val PARTITION = 0

class KafkaControllerTest : FunSpec({

    test("runKafka") {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        PmCreateRequest(
                            pm = PmCreateObject(
                                name = "Ноутбук ASUS ZenBook 14",
                                description = "Тестовая модель товара для проверки create",
                                productGroupId = "product-group-0001"
                            ),
                            debug = PmDebug(
                                mode = PmRequestDebugMode.STUB,
                                stub = PmRequestDebugStubs.SUCCESS,
                            ),
                        )
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<PmCreateResponse>(message.value())

        message.topic() shouldBe outputTopic
        result.pm?.name shouldBe "Ноутбук ASUS ZenBook 14"
    }
})
