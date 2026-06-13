package com.product.model.repo

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock

data class DbPmIdRequest(
    val id: InnerPmId,
    val lock: InnerPmLock = InnerPmLock.NONE,
) {
    constructor(ad: InnerPm) : this(ad.id, ad.lock)
}
