package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение модели продукта из БД"
    on { state == InnerPmState.RUNNING }
    process {
        val request = DbPmIdRequest(pmValidated)
        when (val result = pmRepo.readPm(request)) {
            is DbPmResponseOk -> pmRepoRead = result.data
            is DbPmResponseErr -> fail(result.errors)
            is DbPmResponseErrWithData -> {
                fail(result.errors)
                pmRepoRead = result.data
            }
        }
    }
}
