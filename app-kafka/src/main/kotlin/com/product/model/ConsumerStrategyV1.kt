package com.product.model

import com.product.model.api.v1.models.IRequest
import com.product.model.api.v1.models.IResponse
import com.product.models.mappers.fromTransport
import com.product.models.mappers.toTransportPm

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: InnerPmContext): String {
        val response: IResponse = source.toTransportPm()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: InnerPmContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
