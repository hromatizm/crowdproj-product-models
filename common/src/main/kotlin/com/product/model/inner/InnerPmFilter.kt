package com.product.model.inner

data class InnerPmFilter(
    var name: String = "",
    var description: String = "",
) {
    fun deepCopy(): InnerPmFilter = copy()
}
