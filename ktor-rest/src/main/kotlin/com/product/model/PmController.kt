package com.product.model

import com.product.model.api.v1.models.*
import io.ktor.server.application.*
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createPm::class
suspend fun ApplicationCall.createPm(appSettings: AppSettings) =
    processV1<PmCreateRequest, PmCreateResponse>(appSettings, clCreate, "create")

val clRead: KClass<*> = ApplicationCall::readPm::class
suspend fun ApplicationCall.readPm(appSettings: AppSettings) =
    processV1<PmReadRequest, PmReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updatePm::class
suspend fun ApplicationCall.updatePm(appSettings: AppSettings) =
    processV1<PmUpdateRequest, PmUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deletePm::class
suspend fun ApplicationCall.deletePm(appSettings: AppSettings) =
    processV1<PmDeleteRequest, PmDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchPm::class
suspend fun ApplicationCall.searchPm(appSettings: AppSettings) =
    processV1<PmSearchRequest, PmSearchResponse>(appSettings, clSearch, "search")

