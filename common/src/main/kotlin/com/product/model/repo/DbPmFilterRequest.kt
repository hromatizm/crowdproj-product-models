package com.product.model.repo

import com.product.model.inner.InnerPmUserId

data class DbPmFilterRequest(
    val nameFilter: String = "",
    val descriptionFilter: String = "",
    val ownerId: InnerPmUserId = InnerPmUserId.NONE,
)
