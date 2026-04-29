package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление модели продукта из БД по ID"
    on { state == InnerPmState.RUNNING }
    process {
        val request = DbPmIdRequest(pmRepoPrepare)
        when (val result = pmRepo.deletePm(request)) {
            is DbPmResponseOk -> pmRepoDone = result.data
            is DbPmResponseErr -> {
                fail(result.errors)
                pmRepoDone = pmRepoRead
            }
            is DbPmResponseErrWithData -> {
                fail(result.errors)
                pmRepoDone = result.data
            }
        }
    }
}
