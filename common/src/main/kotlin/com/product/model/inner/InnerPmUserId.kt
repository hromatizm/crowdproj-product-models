package com.product.model.inner

import kotlin.jvm.JvmInline

@JvmInline
value class InnerPmUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerPmUserId("")
    }
}
