package com.product.model

import com.product.model.inner.*
import com.product.model.repo.IRepoPm
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class InnerPmContext(
    var command: InnerPmCommand = InnerPmCommand.NONE,
    var state: InnerPmState = InnerPmState.NONE,
    val errors: MutableList<InnerPmError> = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),
    var workMode: InnerPmWorkMode = InnerPmWorkMode.PROD,
    var stubCase: InnerPmStubs = InnerPmStubs.NONE,

    var pmValidating: InnerPm = InnerPm(),
    var pmFilterValidating: InnerPmFilter = InnerPmFilter(),

    var pmValidated: InnerPm = InnerPm(),
    var pmFilterValidated: InnerPmFilter = InnerPmFilter(),

    var requestId: InnerPmRequestId = InnerPmRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var pmRequest: InnerPm = InnerPm(),
    var pmFilterRequest: InnerPmFilter = InnerPmFilter(),

    var pmRepo: IRepoPm = IRepoPm.NONE,
    var pmRepoRead: InnerPm = InnerPm(), // То, что прочитали из репозитория
    var pmRepoPrepare: InnerPm = InnerPm(), // То, что готовим для сохранения в БД
    var pmRepoDone: InnerPm = InnerPm(),  // Результат, полученный из БД
    var pmsRepoDone: MutableList<InnerPm> = mutableListOf(),

    var pmResponse: InnerPm = InnerPm(),
    var pmsResponse: MutableList<InnerPm> = mutableListOf(),
)
