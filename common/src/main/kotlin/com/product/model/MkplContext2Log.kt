package com.product.model

import com.product.model.api.v1.models.*
import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmFilter
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmProductGroupId
import com.product.model.inner.InnerPmRequestId
import com.product.model.inner.InnerPmUserId
import kotlin.time.Clock

fun InnerPmContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    pm = toPmLog(),
    errors = errors.map { it.toLog() },
)

private fun InnerPmContext.toPmLog(): PmLogModel? {
    val adNone = InnerPm()
    return PmLogModel(
        requestId = requestId.takeIf { it != InnerPmRequestId.NONE }?.asString(),
        requestPm = pmRequest.takeIf { it != adNone }?.toLog(),
        responsePm = pmResponse.takeIf { it != adNone }?.toLog(),
        responsePms = pmsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = pmFilterRequest.takeIf { it != InnerPmFilter() }?.toLog(),
    ).takeIf { it != PmLogModel() }
}

private fun InnerPmFilter.toLog() =
    PmFilterLog(
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() }
    )

private fun InnerPmError.toLog() =
    ErrorLogModel(
        message = message.takeIf { it.isNotBlank() },
        field = field.takeIf { it.isNotBlank() },
        code = code.takeIf { it.isNotBlank() },
        level = level.name,
    )

private fun InnerPm.toLog() =
    PmLog(
        id = id.takeIf { it != InnerPmId.NONE }?.asString(),
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        productGroupId = productGroupId.takeIf { it != InnerPmProductGroupId.NONE }?.asString(),
        ownerId = ownerId.takeIf { it != InnerPmUserId.NONE }?.asString(),
        permissions = permissions.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
    )
