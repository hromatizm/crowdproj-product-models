package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.repo.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoPmDeleteTest {
    abstract val repo: IRepoPm
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = InnerPmId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deletePm(DbPmIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbPmResponseOk>(result)
        assertEquals(deleteSucc.name, result.data.name)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readPm(DbPmIdRequest(notFoundId, lock = lockOld))

        assertIs<DbPmResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deletePm(DbPmIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbPmResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitPms("delete") {
        override val initObjects: List<InnerPm> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
