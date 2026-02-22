package com.product.model.inner

data class InnerPm(
    var id: InnerPmId = InnerPmId.NONE,
    var name: String = "",
    var description: String = "",
    var productGroupId: InnerPmProductGroupId = InnerPmProductGroupId.NONE,
    var ownerId: InnerPmUserId = InnerPmUserId.NONE,
    var deleted: Boolean = false,
    var lock: InnerPmLock = InnerPmLock.NONE,
    val permissions: MutableSet<InnerPmPermission> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = InnerPm()
    }
}
