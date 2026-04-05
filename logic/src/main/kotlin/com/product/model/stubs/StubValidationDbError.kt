package com.product.model.stubs

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmStubs

fun ICorChainDsl<InnerPmContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки базы данных"
    on { stubCase == InnerPmStubs.DB_ERROR && state == InnerPmState.RUNNING }
    process {
        fail(
            InnerPmError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
