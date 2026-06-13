package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.inner.InnerPmUserId
import com.product.model.repo.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPmUpdateTest {
    abstract val repo: IRepoPm
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = InnerPmId("ad-repo-update-not-found")
    protected val lockBad = InnerPmLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = InnerPmLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        InnerPm(
            id = updateSucc.id,
            name = "update object",
            description = "update object description",
            ownerId = InnerPmUserId("owner-123"),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound =
        InnerPm(
            id = updateIdNotFound,
            name = "update object not found",
            description = "update object not found description",
            ownerId = InnerPmUserId("owner-123"),
            lock = initObjects.first().lock,
        )
    private val reqUpdateConc by lazy {
        InnerPm(
            id = updateConc.id,
            name = "update object not found",
            description = "update object not found description",
            ownerId = InnerPmUserId("owner-123"),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updatePm(DbPmRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbPmResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbPmResponseErrWithData)?.errors}")
        assertIs<DbPmResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.name, result.data.name)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updatePm(DbPmRequest(reqUpdateNotFound))
        assertIs<DbPmResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updatePm(DbPmRequest(reqUpdateConc))
        assertIs<DbPmResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitPms("update") {
        override val initObjects: List<InnerPm> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}