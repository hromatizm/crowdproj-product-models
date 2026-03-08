package com.product.model

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(appSettings: AppSettings) {
    routing {
        route("/") {
            get {
                call.respondText("Работает", status = HttpStatusCode.OK)
            }
        }
        route("/v1/pm") {
            post("/create") {
                call.createPm(appSettings)
            }
            post("/read") {
                call.readPm(appSettings)
            }
            post("/update") {
                call.updatePm(appSettings)
            }
            post("/delete") {
                call.deletePm(appSettings)
            }
            post("/search") {
                call.createPm(appSettings)
            }
        }
    }
}
