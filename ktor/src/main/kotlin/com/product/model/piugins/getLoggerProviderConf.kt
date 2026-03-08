package com.product.model.piugins

import com.product.model.logback.pmLoggerLogback
import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.logging.common.PmLoggerProvider

fun Application.getLoggerProviderConf(): PmLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> PmLoggerProvider { pmLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
}
