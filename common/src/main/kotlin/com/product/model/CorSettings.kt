package com.product.model

import com.product.model.repo.IRepoPm

data class CorSettings(
    val loggerProvider: PmLoggerProvider = PmLoggerProvider(),
    val repoStub: IRepoPm = IRepoPm.NONE,
    val repoTest: IRepoPm = IRepoPm.NONE,
    val repoProd: IRepoPm = IRepoPm.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
