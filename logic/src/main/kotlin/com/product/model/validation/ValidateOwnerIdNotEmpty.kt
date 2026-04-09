package com.product.model.validation

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail

fun ICorChainDsl<InnerPmContext>.validateOwnerIdNotEmpty(title: String) = worker {
    this.title = title
    on { pmValidating.ownerId.asString().isEmpty() }
    process {
        fail(
            errorValidation(
                field = "ownerId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
