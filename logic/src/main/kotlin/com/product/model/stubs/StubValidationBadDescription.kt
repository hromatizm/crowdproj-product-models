package com.product.model.stubs

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmStubs

fun ICorChainDsl<InnerPmContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации для описания модели продукта"
    on { stubCase == InnerPmStubs.BAD_DESCRIPTION && state == InnerPmState.RUNNING }
    process {
        fail(
            InnerPmError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
