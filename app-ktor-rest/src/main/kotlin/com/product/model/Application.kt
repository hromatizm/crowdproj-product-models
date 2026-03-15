package com.product.model

import com.product.model.piugins.initRestAppSettings
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.slf4j.event.Level

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module(
    appSettings: AppSettings = initRestAppSettings()
) {
    // Порядок важен
    install(CachingHeaders)
    install(DefaultHeaders)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
    install(CallLogging) {
        level = Level.INFO
    }
    install(ContentNegotiation) {
        json(apiV1Mapper)
    }
    configureRouting(appSettings)
}
