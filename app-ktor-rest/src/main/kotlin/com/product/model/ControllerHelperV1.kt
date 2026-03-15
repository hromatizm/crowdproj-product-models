package com.product.model

import com.product.model.api.v1.models.IRequest
import com.product.model.api.v1.models.IResponse
import com.product.model.helper.controllerHelper
import com.product.models.mappers.fromTransport
import com.product.models.mappers.toTransportPm
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    appSettings: AppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(this@processV1.receive<Q>())
    },
    { this@processV1.respond(toTransportPm() as R) },
    clazz,
    logId,
)
