@file:OptIn(ExperimentalTime::class)

package com.product.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)

val Instant.Companion.NONE
    get() = INSTANT_NONE
