package com.product.model.repo

interface IRepoPm {

    suspend fun createPm(rq: DbPmRequest): IDbPmResponse

    suspend fun readPm(rq: DbPmIdRequest): IDbPmResponse

    suspend fun updatePm(rq: DbPmRequest): IDbPmResponse

    suspend fun deletePm(rq: DbPmIdRequest): IDbPmResponse

    suspend fun searchPm(rq: DbPmFilterRequest): IDbPmsResponse

    companion object {
        val NONE = object : IRepoPm {
            override suspend fun createPm(rq: DbPmRequest): IDbPmResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readPm(rq: DbPmIdRequest): IDbPmResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updatePm(rq: DbPmRequest): IDbPmResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deletePm(rq: DbPmIdRequest): IDbPmResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchPm(rq: DbPmFilterRequest): IDbPmsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
