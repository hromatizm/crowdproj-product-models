package com.product.model.repo

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmError

sealed interface IDbPmResponse : IDbResponse<InnerPm>

data class DbPmResponseOk(
    val data: InnerPm
) : IDbPmResponse

data class DbPmResponseErr(
    val errors: List<InnerPmError> = emptyList()
) : IDbPmResponse {
    constructor(err: InnerPmError) : this(listOf(err))
}

data class DbPmResponseErrWithData(
    val data: InnerPm,
    val errors: List<InnerPmError> = emptyList()
) : IDbPmResponse {
    constructor(ad: InnerPm, err: InnerPmError) : this(ad, listOf(err))
}
