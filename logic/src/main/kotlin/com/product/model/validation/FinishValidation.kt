package com.product.model.validation

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.finishPmValidation(title: String) = worker {
    this.title = title
    on { state == InnerPmState.RUNNING }
    process {
        pmValidated = pmValidating
    }
}

fun ICorChainDsl<InnerPmContext>.finishPmFilterValidation(title: String) = worker {
    this.title = title
    on { state == InnerPmState.RUNNING }
    process {
        pmFilterValidated = pmFilterValidating
    }
}
