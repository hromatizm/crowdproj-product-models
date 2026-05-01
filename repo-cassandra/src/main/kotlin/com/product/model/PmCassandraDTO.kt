package com.product.model

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.product.model.inner.*
import java.util.*

@Entity
data class PmCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey // можно задать порядок
    var id: UUID? = null,

    @field:CqlName(COLUMN_NAME)
    var name: String? = null,

    @field:CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,

    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,

    @field:CqlName(COLUMN_PRODUCT_GROUP)
    var productGroupId: String? = null,

    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(pmModel: InnerPm) : this(
        id = pmModel.id.takeIf { it != InnerPmId.NONE }?.asString()?.let { UUID.fromString(it) },
        name = pmModel.name.takeIf { it.isNotBlank() },
        description = pmModel.description.takeIf { it.isNotBlank() },
        productGroupId = pmModel.productGroupId.takeIf { it != InnerPmProductGroupId.NONE }?.asString(),
        ownerId = pmModel.ownerId.takeIf { it != InnerPmUserId.NONE }?.asString(),
        lock = pmModel.lock.takeIf { it != InnerPmLock.NONE }?.asString()
    )

    fun toPmModel() =
        InnerPm(
            id = id?.let { InnerPmId(it.toString()) } ?: InnerPmId.NONE,
            name = name ?: "",
            description = description ?: "",
            productGroupId = productGroupId?.let { InnerPmProductGroupId(it) } ?: InnerPmProductGroupId.NONE,
            ownerId = ownerId?.let { InnerPmUserId(it) } ?: InnerPmUserId.NONE,
            lock = lock?.let { InnerPmLock(it) } ?: InnerPmLock.NONE
        )

    companion object {
        const val TABLE_NAME = "product_models"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_OWNER_ID = "owner_id"
        const val COLUMN_PRODUCT_GROUP = "product_group_id"
        const val COLUMN_LOCK = "lock"
    }
}
