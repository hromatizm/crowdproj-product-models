package com.product.model.stubs

import chain.ICorChainDsl
import chain.worker
import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.LogLevel
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmStubs

fun ICorChainDsl<InnerPmContext>.stubReadSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = "Кейс успешного чтения модели продукта"
    on { stubCase == InnerPmStubs.SUCCESS && state == InnerPmState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    process {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = InnerPmState.FINISHING
            val stub = PmStub.prepareResult {
                pmRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
            }
            pmResponse = stub
        }
    }
}
