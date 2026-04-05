package com.product.model.general

import chain.ICorChainDsl
import chain.chain
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmCommand
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.operation(
    title: String,
    command: InnerPmCommand,
    block: ICorChainDsl<InnerPmContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == InnerPmState.RUNNING }
}
