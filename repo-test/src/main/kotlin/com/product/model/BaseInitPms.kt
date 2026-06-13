package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.inner.InnerPmUserId

abstract class BaseInitPms(private val op: String) : IInitObjects<InnerPm> {
    open val lockOld: InnerPmLock = InnerPmLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: InnerPmLock = InnerPmLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: InnerPmUserId = InnerPmUserId("owner-123"),
        lock: InnerPmLock = lockOld,
    ) = InnerPm(
        id = InnerPmId("ad-repo-$op-$suf"),
        name = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        lock = lock,
    )
}
