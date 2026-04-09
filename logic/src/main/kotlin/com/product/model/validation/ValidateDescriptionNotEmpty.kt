package com.product.model.validation

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail

fun ICorChainDsl<InnerPmContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { pmValidating.description.isEmpty() }
    process {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
