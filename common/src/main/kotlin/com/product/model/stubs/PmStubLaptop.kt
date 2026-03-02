package com.product.model.stubs

import com.product.model.inner.*

object PmStubLaptop {
    val PM_LAPTOP_ASUS_ZEN_BOOK_14: InnerPm
        get() = InnerPm(
            id = InnerPmId("pm-1"),
            name = "Ноутбук ASUS ZenBook 14",
            description = "Ноутбук ASUS Zenbook 14, OLED UM3406KA 14 дюйм, AMD Ryzen AI 5 340, RAM 16 ГБ, SSD 1024 ГБ, AMD Radeon Graphics",
            productGroupId = InnerPmProductGroupId("laptop-1"),
            ownerId = InnerPmUserId("user-1"),
            lock = InnerPmLock("lock-1"),
            permissions = mutableSetOf(
                InnerPmPermission.READ,
                InnerPmPermission.UPDATE,
                InnerPmPermission.DELETE,
            )
        )
}
