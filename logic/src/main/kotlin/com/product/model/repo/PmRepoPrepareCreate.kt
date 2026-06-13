package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState
import com.product.model.stubs.PmStub

fun ICorChainDsl<InnerPmContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == InnerPmState.RUNNING }
    process {
        pmRepoPrepare = pmValidated.deepCopy()
    }
}
