package com.product.model

import com.product.model.inner.InnerPmState
import com.product.model.stubs.PmStub

@Suppress("unused", "RedundantSuspendModifier")
class PmProcessor(val corSettings: CorSettings) {

    suspend fun exec(ctx: InnerPmContext) {
        ctx.pmResponse = PmStub.get()
        ctx.pmsResponse = PmStub.prepareSearchList("ad search").toMutableList()
        ctx.state = InnerPmState.RUNNING
    }
}
