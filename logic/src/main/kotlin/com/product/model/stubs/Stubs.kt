package com.product.model.stubs

import chain.ICorChainDsl
import chain.chain
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmWorkMode

fun ICorChainDsl<InnerPmContext>.stubs(title: String, block: ICorChainDsl<InnerPmContext>.() -> Unit) =
    chain {
        block()
        this.title = title
        on { workMode == InnerPmWorkMode.STUB && state == InnerPmState.RUNNING }
    }
