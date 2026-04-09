package com.product.model.stubs

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = "Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах"
    on { state == InnerPmState.RUNNING }
    process {
        fail(
            InnerPmError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
