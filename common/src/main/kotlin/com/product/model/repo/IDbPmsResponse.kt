package com.product.model.repo

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmError

sealed interface IDbPmsResponse : IDbResponse<List<InnerPm>>

data class DbPmsResponseOk(
    val data: List<InnerPm>
) : IDbPmsResponse

@Suppress("unused")
data class DbPmsResponseErr(
    val errors: List<InnerPmError> = emptyList()
) : IDbPmsResponse {
    constructor(err: InnerPmError) : this(listOf(err))
}
