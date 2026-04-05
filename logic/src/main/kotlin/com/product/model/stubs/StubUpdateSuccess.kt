package com.product.model.stubs

import chain.ICorChainDsl
import chain.worker
import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.LogLevel
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmProductGroupId
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmStubs
import com.product.model.inner.InnerPmUserId

fun ICorChainDsl<InnerPmContext>.stubUpdateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = "Кейс успешного изменения модели продукта"
    on { stubCase == InnerPmStubs.SUCCESS && state == InnerPmState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    process {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = InnerPmState.FINISHING
            val stub = PmStub.prepareResult {
                pmRequest.id.takeIf { it != InnerPmId.NONE }?.also { this.id = it }
                pmRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
                pmRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                pmRequest.productGroupId.takeIf { it != InnerPmProductGroupId.NONE }?.also { this.productGroupId = it }
                pmRequest.ownerId.takeIf { it != InnerPmUserId.NONE }?.also { this.ownerId = it }
            }
            pmResponse = stub
        }
    }
}
