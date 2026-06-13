package com.product.model.repo.exceptions

import com.product.model.inner.InnerPmId

class RepoEmptyLockException(
    id: InnerPmId
) : RepoPmException(
    adId = id,
    msg = "Lock is empty in DB"
)