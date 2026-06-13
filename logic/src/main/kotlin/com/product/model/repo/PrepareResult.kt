package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmWorkMode

fun ICorChainDsl<InnerPmContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != InnerPmWorkMode.STUB }
    process {
        pmResponse = pmRepoDone
        pmsResponse = pmsRepoDone
        state = when (val st = state) {
            InnerPmState.RUNNING -> InnerPmState.FINISHING
            else -> st
        }
    }
}
