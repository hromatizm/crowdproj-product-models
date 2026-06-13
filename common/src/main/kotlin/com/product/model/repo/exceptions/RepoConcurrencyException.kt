package com.product.model.repo.exceptions

import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock

class RepoConcurrencyException(
    id: InnerPmId, expectedLock: InnerPmLock, actualLock: InnerPmLock?
) : RepoPmException(
    adId = id,
    msg = "Expected lock is $expectedLock while actual lock in db is $actualLock"
)