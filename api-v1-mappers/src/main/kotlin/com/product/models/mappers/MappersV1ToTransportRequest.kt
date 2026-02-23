package com.product.models.mappers

import com.product.model.api.v1.models.PmCreateObject
import com.product.model.api.v1.models.PmDeleteObject
import com.product.model.api.v1.models.PmReadObject
import com.product.model.api.v1.models.PmUpdateObject
import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmLock
import com.product.model.inner.InnerPmProductGroupId

fun InnerPm.toTransportCreatePm() =
    PmCreateObject(
        name = name,
        description = description,
        productGroupId = productGroupId.toTransportPm(),
    )

fun InnerPm.toTransportReadPm() =
    PmReadObject(
        id = id.toTransportPm()
    )

fun InnerPm.toTransportUpdatePm() =
    PmUpdateObject(
        pmId = id.toTransportPm(),
        name = name,
        description = description,
        productGroupId = productGroupId.toTransportPm(),
        pmLock = lock.toTransportPm(),
    )

fun InnerPm.toTransportDeletePm() =
    PmDeleteObject(
        id = id.toTransportPm(),
        lock = lock.toTransportPm(),
    )

internal fun InnerPmLock.toTransportPm() = takeIf { it != InnerPmLock.NONE }?.asString()

internal fun InnerPmProductGroupId.toTransportPm() = takeIf { it != InnerPmProductGroupId.NONE }?.asString()
