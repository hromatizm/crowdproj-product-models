package com.product.model

data class CorSettings(
    val loggerProvider: PmLoggerProvider = PmLoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
