package com.product.model.repo.exceptions

import com.product.model.inner.InnerPmId

open class RepoPmException(
    @Suppress("unused")
    val adId: InnerPmId,
    msg: String,
) : RepoException(msg)
