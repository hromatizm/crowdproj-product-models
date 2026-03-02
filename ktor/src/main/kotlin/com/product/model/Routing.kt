package com.product.model

import com.product.model.api.v1.models.IRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                call.respondText("Работает", status = HttpStatusCode.OK)
            }
        }
        route("/v1/pm") {
            post("/create") {
                val req = call.receive<IRequest>()
                call.respondText("Модель продукта сохранена", status = HttpStatusCode.Created)
            }
            post("/read") {
                val req = call.receive<IRequest>()
                call.respondText("Модель продукта найдена", status = HttpStatusCode.OK)
            }
            post("/update") {
                val req = call.receive<IRequest>()
                call.respondText("Модель продукта обновлена", status = HttpStatusCode.OK)
            }
            post("/delete") {
                val req = call.receive<IRequest>()
                call.respondText("Модель продукта удалена", status = HttpStatusCode.OK)
            }
            post("/search") {
                val req = call.receive<IRequest>()
                call.respondText("Рузультат поиска", status = HttpStatusCode.OK)
            }
        }
    }
}
