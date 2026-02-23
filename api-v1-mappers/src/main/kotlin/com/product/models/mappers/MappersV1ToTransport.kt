package com.product.models.mappers

import com.product.model.InnerPmContext
import com.product.model.api.v1.models.*
import com.product.model.inner.*
import ru.otus.otuskotlin.marketplace.common.exceptions.UnknownPmCommand

fun InnerPmContext.toTransportPm(): IResponse = when (val cmd = command) {
    InnerPmCommand.CREATE -> toTransportCreate()
    InnerPmCommand.READ -> toTransportRead()
    InnerPmCommand.UPDATE -> toTransportUpdate()
    InnerPmCommand.DELETE -> toTransportDelete()
    InnerPmCommand.SEARCH -> toTransportSearch()
    InnerPmCommand.NONE -> throw UnknownPmCommand(cmd)
}

fun InnerPmContext.toTransportCreate() =
    PmCreateResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        pm = pmResponse.toTransportPm(),
    )

fun InnerPmContext.toTransportRead() =
    PmReadResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        pm = pmResponse.toTransportPm()
    )

fun InnerPmContext.toTransportUpdate() =
    PmUpdateResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        pm = pmResponse.toTransportPm()
    )

fun InnerPmContext.toTransportDelete() =
    PmDeleteResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        pm = pmResponse.toTransportPm()
    )

fun InnerPmContext.toTransportSearch() =
    PmSearchResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        pms = pmsResponse.toTransportPm()
    )

fun List<InnerPm>.toTransportPm(): List<PmResponseObject>? = this
    .map { it.toTransportPm() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun InnerPm.toTransportPm(): PmResponseObject =
    PmResponseObject(
        id = id.toTransportPm(),
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        ownerId = ownerId.takeIf { it != InnerPmUserId.NONE }?.asString(),
        productGroupId = productGroupId.takeIf { it != InnerPmProductGroupId.NONE }?.asString(),
        lock = lock.takeIf { it != InnerPmLock.NONE }?.asString(),
        permissions = permissions.toTransportPm(),
    )

internal fun InnerPmId.toTransportPm() = takeIf { it != InnerPmId.NONE }?.asString()

private fun Set<InnerPmPermission>.toTransportPm(): Set<PmPermission>? = this
    .map { it.toTransportPm() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun InnerPmPermission.toTransportPm() = when (this) {
    InnerPmPermission.READ -> PmPermission.READ
    InnerPmPermission.UPDATE -> PmPermission.UPDATE
    InnerPmPermission.DELETE -> PmPermission.DELETE
}

private fun List<InnerPmError>.toTransportErrors(): List<PmError>? = this
    .map { it.toTransportPm() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun InnerPmError.toTransportPm() = PmError(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun InnerPmState.toResult(): ResponseResult? = when (this) {
    InnerPmState.RUNNING -> ResponseResult.SUCCESS
    InnerPmState.FAILING -> ResponseResult.ERROR
    InnerPmState.FINISHING -> ResponseResult.SUCCESS
    InnerPmState.NONE -> null
}
