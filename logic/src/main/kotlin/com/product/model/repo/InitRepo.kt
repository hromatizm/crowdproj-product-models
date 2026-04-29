package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.exceptions.PmDbNotConfiguredException
import com.product.model.helper.errorSystem
import com.product.model.helper.fail
import com.product.model.inner.InnerPmWorkMode

fun ICorChainDsl<InnerPmContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы"
    process {
        pmRepo = when (workMode) {
            InnerPmWorkMode.TEST -> corSettings.repoTest
            InnerPmWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != InnerPmWorkMode.STUB && pmRepo == IRepoPm.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = PmDbNotConfiguredException(workMode)
            )
        )
    }
}
