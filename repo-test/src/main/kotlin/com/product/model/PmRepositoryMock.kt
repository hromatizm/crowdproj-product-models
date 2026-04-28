package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.repo.*

class PmRepositoryMock(
    private val invokeCreatePm: (DbPmRequest) -> IDbPmResponse = { DEFAULT_PM_SUCCESS_EMPTY_MOCK },
    private val invokeReadPm: (DbPmIdRequest) -> IDbPmResponse = { DEFAULT_PM_SUCCESS_EMPTY_MOCK },
    private val invokeUpdatePm: (DbPmRequest) -> IDbPmResponse = { DEFAULT_PM_SUCCESS_EMPTY_MOCK },
    private val invokeDeletePm: (DbPmIdRequest) -> IDbPmResponse = { DEFAULT_PM_SUCCESS_EMPTY_MOCK },
    private val invokeSearchPm: (DbPmFilterRequest) -> IDbPmsResponse = { DEFAULT_PMS_SUCCESS_EMPTY_MOCK },
) : IRepoPm {
    override suspend fun createPm(rq: DbPmRequest): IDbPmResponse {
        return invokeCreatePm(rq)
    }

    override suspend fun readPm(rq: DbPmIdRequest): IDbPmResponse {
        return invokeReadPm(rq)
    }

    override suspend fun updatePm(rq: DbPmRequest): IDbPmResponse {
        return invokeUpdatePm(rq)
    }

    override suspend fun deletePm(rq: DbPmIdRequest): IDbPmResponse {
        return invokeDeletePm(rq)
    }

    override suspend fun searchPm(rq: DbPmFilterRequest): IDbPmsResponse {
        return invokeSearchPm(rq)
    }

    companion object {
        val DEFAULT_PM_SUCCESS_EMPTY_MOCK = DbPmResponseOk(InnerPm())
        val DEFAULT_PMS_SUCCESS_EMPTY_MOCK = DbPmsResponseOk(emptyList())
    }
}
