package com.product.model.helper

import com.product.model.inner.InnerPmError

fun Throwable.asPmError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = InnerPmError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
