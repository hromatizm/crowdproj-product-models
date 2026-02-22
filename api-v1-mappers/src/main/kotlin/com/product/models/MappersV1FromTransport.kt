package com.product.models

import com.product.model.InnerPmContext
import com.product.model.api.v1.models.*
import com.product.model.inner.*
import com.product.models.exceptions.UnknownRequestClass

fun InnerPmContext.fromTransport(request: IRequest) = when (request) {
    is PmCreateRequest -> fromTransport(request)
    is PmReadRequest -> fromTransport(request)
    is PmUpdateRequest -> fromTransport(request)
    is PmDeleteRequest -> fromTransport(request)
    is PmSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toInnerPmId() = this?.let { InnerPmId(it) } ?: InnerPmId.NONE
private fun String?.toInnerPmLock() = this?.let { InnerPmLock(it) } ?: InnerPmLock.NONE
private fun String?.toInnerPmProductGroupId() = this?.let { InnerPmProductGroupId(it) } ?: InnerPmProductGroupId.NONE


private fun PmDebug?.transportToWorkMode(): InnerPmWorkMode = when (this?.mode) {
    PmRequestDebugMode.PROD -> InnerPmWorkMode.PROD
    PmRequestDebugMode.TEST -> InnerPmWorkMode.TEST
    PmRequestDebugMode.STUB -> InnerPmWorkMode.STUB
    null -> InnerPmWorkMode.PROD
}

private fun PmDebug?.transportToStubCase(): InnerPmStubs = when (this?.stub) {
    PmRequestDebugStubs.SUCCESS -> InnerPmStubs.SUCCESS
    PmRequestDebugStubs.NOT_FOUND -> InnerPmStubs.NOT_FOUND
    PmRequestDebugStubs.BAD_ID -> InnerPmStubs.BAD_ID
    PmRequestDebugStubs.BAD_TITLE -> InnerPmStubs.BAD_TITLE
    PmRequestDebugStubs.BAD_DESCRIPTION -> InnerPmStubs.BAD_DESCRIPTION
    PmRequestDebugStubs.CANNOT_DELETE -> InnerPmStubs.CANNOT_DELETE
    PmRequestDebugStubs.BAD_SEARCH_STRING -> InnerPmStubs.BAD_SEARCH_STRING
    null -> InnerPmStubs.NONE
}

fun InnerPmContext.fromTransport(request: PmReadRequest) {
    command = InnerPmCommand.READ
    pmRequest = request.pm.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PmReadObject?.toInternal(): InnerPm = if (this != null) {
    InnerPm(id = id.toInnerPmId())
} else {
    InnerPm()
}

fun InnerPmContext.fromTransport(request: PmCreateRequest) {
    command = InnerPmCommand.CREATE
    pmRequest = request.pm?.toInternal() ?: InnerPm()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun InnerPmContext.fromTransport(request: PmUpdateRequest) {
    command = InnerPmCommand.UPDATE
    pmRequest = request.pm?.toInternal() ?: InnerPm()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun InnerPmContext.fromTransport(request: PmDeleteRequest) {
    command = InnerPmCommand.DELETE
    pmRequest = request.pm.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PmDeleteObject?.toInternal(): InnerPm = if (this != null) {
    InnerPm(
        id = id.toInnerPmId(),
        lock = lock.toInnerPmLock(),
    )
} else {
    InnerPm()
}

fun InnerPmContext.fromTransport(request: PmSearchRequest) {
    command = InnerPmCommand.SEARCH
    pmFilterRequest = request.pmFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PmSearchFilter?.toInternal() =
    InnerPmFilter(
        name = this?.name ?: "",
        description = this?.description ?: "",
    )

private fun PmCreateObject.toInternal() =
    InnerPm(
        name = this.name ?: "",
        description = this.description ?: "",
        productGroupId = this.productGroupId.toInnerPmProductGroupId(),
    )

private fun PmUpdateObject.toInternal() =
    InnerPm(
        id = this.pmId.toInnerPmId(),
        name = this.name ?: "",
        description = this.description ?: "",
        productGroupId = this.productGroupId.toInnerPmProductGroupId(),
        lock = this.pmLock.toInnerPmLock(),
    )