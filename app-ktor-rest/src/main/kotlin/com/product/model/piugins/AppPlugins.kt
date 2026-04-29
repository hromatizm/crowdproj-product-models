package com.product.model.piugins

import com.product.model.AppSettings
import com.product.model.CorSettings
import com.product.model.PmRepoStub
import com.product.model.processor.PmProcessor
import io.ktor.server.application.*

fun Application.initRestAppSettings(): AppSettings {
    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(PmDbType.TEST),
        repoProd = getDatabaseConf(PmDbType.PROD),
        repoStub = PmRepoStub(),
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = PmProcessor(corSettings),
    )
}

fun Application.initStubAppSettings(): AppSettings {
    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf()
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = PmProcessor(corSettings),
    )
}