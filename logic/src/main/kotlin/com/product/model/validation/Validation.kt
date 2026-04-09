package com.product.model.validation

import chain.ICorChainDsl
import chain.chain
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.validation(block: ICorChainDsl<InnerPmContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == InnerPmState.RUNNING }
}
