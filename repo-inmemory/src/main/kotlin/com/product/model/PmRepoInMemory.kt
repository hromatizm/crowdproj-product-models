package com.product.model

import com.benasher44.uuid.uuid4
import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.inner.InnerPmUserId
import com.product.model.repo.DbPmFilterRequest
import com.product.model.repo.DbPmIdRequest
import com.product.model.repo.DbPmRequest
import com.product.model.repo.DbPmResponseOk
import com.product.model.repo.DbPmsResponseOk
import com.product.model.repo.IDbPmResponse
import com.product.model.repo.IDbPmsResponse
import com.product.model.repo.IRepoPm
import com.product.model.repo.PmRepoBase
import com.product.model.repo.errorDb
import com.product.model.repo.errorEmptyId
import com.product.model.repo.errorEmptyLock
import com.product.model.repo.errorNotFound
import com.product.model.repo.errorRepoConcurrency
import com.product.model.repo.exceptions.RepoEmptyLockException
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PmRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : PmRepoBase(), IRepoPm, IRepoPmInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, PmEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(pms: Collection<InnerPm>) = pms.map { pm ->
        val entity = PmEntity(pm)
        require(entity.id != null)
        cache.put(entity.id, entity)
        pm
    }

    override suspend fun createPm(rq: DbPmRequest): IDbPmResponse = tryPmMethod {
        val key = randomUuid()
        val pm = rq.pm.copy(id = InnerPmId(key), lock = InnerPmLock(randomUuid()))
        val entity = PmEntity(pm)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbPmResponseOk(pm)
    }

    override suspend fun readPm(rq: DbPmIdRequest): IDbPmResponse = tryPmMethod {
        val key = rq.id.takeIf { it != InnerPmId.NONE }?.asString() ?: return@tryPmMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbPmResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updatePm(rq: DbPmRequest): IDbPmResponse = tryPmMethod {
        val rqPm = rq.pm
        val id = rqPm.id.takeIf { it != InnerPmId.NONE } ?: return@tryPmMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqPm.lock.takeIf { it != InnerPmLock.NONE } ?: return@tryPmMethod errorEmptyLock(id)

        mutex.withLock {
            val oldPm = cache.get(key)?.toInternal()
            when {
                oldPm == null -> errorNotFound(id)
                oldPm.lock == InnerPmLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldPm.lock != oldLock -> errorRepoConcurrency(oldPm, oldLock)
                else -> {
                    val newPm = rqPm.copy(lock = InnerPmLock(randomUuid()))
                    val entity = PmEntity(newPm)
                    cache.put(key, entity)
                    DbPmResponseOk(newPm)
                }
            }
        }
    } 

    override suspend fun deletePm(rq: DbPmIdRequest): IDbPmResponse = tryPmMethod {
        val id = rq.id.takeIf { it != InnerPmId.NONE } ?: return@tryPmMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != InnerPmLock.NONE } ?: return@tryPmMethod errorEmptyLock(id)

        mutex.withLock {
            val oldPm = cache.get(key)?.toInternal()
            when {
                oldPm == null -> errorNotFound(id)
                oldPm.lock == InnerPmLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldPm.lock != oldLock -> errorRepoConcurrency(oldPm, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbPmResponseOk(oldPm)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchPm(rq: DbPmFilterRequest): IDbPmsResponse = tryPmsMethod {
        val result: List<InnerPm> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != InnerPmUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbPmsResponseOk(result)
    }
}
