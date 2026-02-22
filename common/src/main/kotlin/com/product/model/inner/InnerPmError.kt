package com.product.model.inner

data class InnerPmError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: LogLevel = LogLevel.ERROR,
) {
    enum class LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
