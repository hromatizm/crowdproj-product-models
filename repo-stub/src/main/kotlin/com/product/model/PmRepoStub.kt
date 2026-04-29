package com.product.model

import com.product.model.inner.InnerPmFilter
import com.product.model.repo.*
import com.product.model.stubs.PmStub

class PmRepoStub() : IRepoPm {
    override suspend fun createPm(rq: DbPmRequest): IDbPmResponse {
        return DbPmResponseOk(
            data = PmStub.get(),
        )
    }

    override suspend fun readPm(rq: DbPmIdRequest): IDbPmResponse {
        return DbPmResponseOk(
            data = PmStub.get(),
        )
    }

    override suspend fun updatePm(rq: DbPmRequest): IDbPmResponse {
        return DbPmResponseOk(
            data = PmStub.get(),
        )
    }

    override suspend fun deletePm(rq: DbPmIdRequest): IDbPmResponse {
        return DbPmResponseOk(
            data = PmStub.get(),
        )
    }

    override suspend fun searchPm(rq: DbPmFilterRequest): IDbPmsResponse {
        return DbPmsResponseOk(
            data = PmStub.prepareSearchList(
                filter = InnerPmFilter(
                    name = "",
                    description = "",
                )
            ),
        )
    }
}
