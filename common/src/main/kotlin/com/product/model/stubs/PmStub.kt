package com.product.model.stubs

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.stubs.PmStubLaptop.PM_LAPTOP_ASUS_ZEN_BOOK_14

object PmStub {
    fun get(): InnerPm = PM_LAPTOP_ASUS_ZEN_BOOK_14.copy()

    fun prepareResult(block: InnerPm.() -> Unit): InnerPm = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        innerPm("d-666-01", filter),
        innerPm("d-666-02", filter),
        innerPm("d-666-03", filter),
        innerPm("d-666-04", filter),
        innerPm("d-666-05", filter),
        innerPm("d-666-06", filter),
    )

    private fun innerPm(id: String, filter: String) = PM_LAPTOP_ASUS_ZEN_BOOK_14.copy(
        id = InnerPmId(id),
        name = "$filter $id",
        description = "description $filter $id",
    )
}
