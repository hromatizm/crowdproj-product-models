package com.product.model

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.repo.*
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.*

class RepoPmCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val dc: String = "datacenter1",
    private val randomUuid: () -> String = { uuid4().toString() },
) : PmRepoBase(), IRepoPm, IRepoPmInitializable {

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default")
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter(dc)
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .withKeyspace(keyspaceName)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private val dao by lazy {
        mapper.pmDao(keyspaceName, PmCassandraDTO.TABLE_NAME)
    }

    fun clear() = dao.deleteAll()

    override fun save(pms: Collection<InnerPm>): Collection<InnerPm> = runBlocking {
        pms.onEach { dao.create(PmCassandraDTO(it)).await() }
    }

    override suspend fun createPm(rq: DbPmRequest): IDbPmResponse = tryPmMethod {
        val new = rq.pm.copy(id = InnerPmId(randomUuid()), lock = InnerPmLock(randomUuid()))
        dao.create(PmCassandraDTO(new)).await()
        DbPmResponseOk(new)
    }

    override suspend fun readPm(rq: DbPmIdRequest): IDbPmResponse = tryPmMethod {
        if (rq.id == InnerPmId.NONE) return@tryPmMethod errorEmptyId
        val res = dao.read(rq.id.asString()).await()
            ?: return@tryPmMethod errorNotFound(rq.id)
        DbPmResponseOk(res.toPmModel())
    }

    override suspend fun updatePm(rq: DbPmRequest): IDbPmResponse = tryPmMethod {
        val idStr = rq.pm.id.asString()
        val prevLock = rq.pm.lock.asString()
        val new = rq.pm.copy(lock = InnerPmLock(randomUuid()))
        val dto = PmCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(PmCassandraDTO.COLUMN_LOCK) }
            ?.getString(PmCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbPmResponseOk(new)
            // res.wasApplied() -> DbPmResponse.success(dao.read(idStr).await()?.toAdModel())
            resultField == null -> errorNotFound(rq.pm.id)
            else -> errorRepoConcurrency(
                oldPm = dao.read(idStr).await()?.toPmModel()
                    ?: throw Exception(
                        "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                                "was denied for update but the same object was not found in db at further request"
                    ),
                expectedLock = rq.pm.lock
            )
        }
    }

    override suspend fun deletePm(rq: DbPmIdRequest): IDbPmResponse = tryPmMethod {
        val idStr = rq.id.asString()
        val prevLock = rq.lock.asString()
        val oldAd = dao.read(idStr).await()?.toPmModel() ?: return@tryPmMethod errorNotFound(rq.id)
        val res = dao.delete(idStr, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(PmCassandraDTO.COLUMN_LOCK) }
            ?.getString(PmCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbPmResponseOk(oldAd)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(idStr).await()?.toPmModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    override suspend fun searchPm(rq: DbPmFilterRequest): IDbPmsResponse = tryPmsMethod {
        val found = dao.search(rq).await()
        DbPmsResponseOk(found.map { it.toPmModel() })
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}
