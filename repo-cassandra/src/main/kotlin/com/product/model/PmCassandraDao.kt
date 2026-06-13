package com.product.model

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import com.product.model.PmCassandraDTO.Companion.COLUMN_LOCK
import com.product.model.repo.DbPmFilterRequest
import java.util.UUID
import java.util.concurrent.CompletionStage

@Dao
interface PmCassandraDao {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: PmCassandraDTO): CompletionStage<PmCassandraDTO>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(id: String): CompletionStage<PmCassandraDTO?>

    @Update(customIfClause = "${COLUMN_LOCK} = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: PmCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "${COLUMN_LOCK} = :prevLock", entityClass = [PmCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @Query("TRUNCATE ${PmCassandraDTO.TABLE_NAME}")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun deleteAll()

    @QueryProvider(providerClass = AdCassandraSearchProvider::class, entityHelpers = [PmCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun search(filter: DbPmFilterRequest): CompletionStage<Collection<PmCassandraDTO>>
}
