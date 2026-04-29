package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД"
    on { state == InnerPmState.RUNNING }
    process {
        pmRepoPrepare = pmValidated.deepCopy()
    }
}
