package com.product.model.piugins

import com.product.model.AppSettings
import com.product.model.CorSettings
import com.product.model.PmProcessor
import io.ktor.server.application.*

fun Application.initAppSettings(): AppSettings {
    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf()
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = PmProcessor(corSettings),
    )
}