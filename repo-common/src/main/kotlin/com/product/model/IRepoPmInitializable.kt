package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.repo.IRepoPm

interface IRepoPmInitializable : IRepoPm {

    fun save(pms: Collection<InnerPm>): Collection<InnerPm>
}
