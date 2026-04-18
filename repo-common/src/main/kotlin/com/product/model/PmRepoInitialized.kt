package com.product.model

import com.product.model.inner.InnerPm
import kotlin.collections.toList

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class PmRepoInitialized(
    val repo: IRepoPmInitializable,
    initObjects: Collection<InnerPm> = emptyList(),
) : IRepoPmInitializable by repo {

    @Suppress("unused")
    val initializedObjects: List<InnerPm> = save(initObjects).toList()
}
