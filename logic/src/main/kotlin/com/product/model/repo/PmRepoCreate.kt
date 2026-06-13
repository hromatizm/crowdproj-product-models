package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление модели продукта в БД"
    on { state == InnerPmState.RUNNING }
    process {
        val request = DbPmRequest(pmRepoPrepare)
        when (val result = pmRepo.createPm(request)) {
            is DbPmResponseOk -> pmRepoDone = result.data
            is DbPmResponseErr -> fail(result.errors)
            is DbPmResponseErrWithData -> {
                fail(result.errors)
                pmRepoDone = result.data
            }
        }
    }
}
