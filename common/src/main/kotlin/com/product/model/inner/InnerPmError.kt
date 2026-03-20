package com.product.model.inner

import com.product.model.LogLevel

data class InnerPmError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: LogLevel = LogLevel.ERROR,
)
