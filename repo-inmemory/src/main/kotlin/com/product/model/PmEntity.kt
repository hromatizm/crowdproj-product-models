package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.inner.InnerPmUserId

data class PmEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val lock: String? = null,
) {
    constructor(model: InnerPm) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = InnerPm(
        id = id?.let { InnerPmId(it) } ?: InnerPmId.NONE,
        name = name ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { InnerPmUserId(it) } ?: InnerPmUserId.NONE,
        lock = lock?.let { InnerPmLock(it) } ?: InnerPmLock.NONE,
    )
}
