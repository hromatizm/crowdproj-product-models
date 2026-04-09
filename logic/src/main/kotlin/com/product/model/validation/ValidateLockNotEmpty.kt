package com.product.model.validation

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail

fun ICorChainDsl<InnerPmContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { pmValidating.lock.asString().isEmpty() }
    process {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
