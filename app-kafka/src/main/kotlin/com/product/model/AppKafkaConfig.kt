package com.product.model

import com.product.model.logback.pmLoggerLogback
import ru.otus.otuskotlin.marketplace.logging.common.PmLoggerProvider

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    override val corSettings: CorSettings = CorSettings(
        loggerProvider = PmLoggerProvider { pmLoggerLogback(it) }
    ),
    override val processor: PmProcessor = PmProcessor(corSettings),
): IAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy {
            (System.getenv(KAFKA_HOST_VAR) ?: "localhost:9092")
                .split(Regex("\\s*[,; ]\\s*"))
                .filter { it.isNotBlank() }
        }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "pm-group" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "pm-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "pm-v1-out" }
    }
}
