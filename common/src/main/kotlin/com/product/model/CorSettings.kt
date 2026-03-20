package com.product.model

import ru.otus.otuskotlin.marketplace.logging.common.PmLoggerProvider

data class CorSettings(
    val loggerProvider: PmLoggerProvider = PmLoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
