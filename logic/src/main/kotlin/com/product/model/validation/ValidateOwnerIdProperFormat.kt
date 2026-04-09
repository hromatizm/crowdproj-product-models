package com.product.model.validation

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmUserId

fun ICorChainDsl<InnerPmContext>.validateOwnerIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { pmValidating.ownerId != InnerPmUserId.NONE && !pmValidating.ownerId.asString().matches(regExp) }
    process {
        val encodedId = pmValidating.ownerId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "ownerId",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
