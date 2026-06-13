package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>. repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск моделей продуктов в БД по фильтру"
    on { state == InnerPmState.RUNNING }
    process {
        val request = DbPmFilterRequest(
            nameFilter = pmFilterValidated.name,
            descriptionFilter = pmFilterValidated.description,
        )
        when (val result = pmRepo.searchPm(request)) {
            is DbPmsResponseOk -> pmsRepoDone = result.data.toMutableList()
            is DbPmsResponseErr -> fail(result.errors)
        }
    }
}
