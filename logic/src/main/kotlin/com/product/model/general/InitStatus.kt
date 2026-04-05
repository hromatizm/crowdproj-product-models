package com.product.model.general

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description =
        "Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса"
    on { state == InnerPmState.NONE }
    process { state = InnerPmState.RUNNING }
}
