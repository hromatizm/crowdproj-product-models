package com.product.model.inner

import kotlin.jvm.JvmInline

@JvmInline
value class InnerPmId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerPmId("")
    }
}
