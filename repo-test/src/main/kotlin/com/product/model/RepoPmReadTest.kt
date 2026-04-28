package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmId
import com.product.model.repo.DbPmIdRequest
import com.product.model.repo.DbPmResponseErr
import com.product.model.repo.DbPmResponseOk
import com.product.model.repo.IRepoPm
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPmReadTest {
    abstract val repo: IRepoPm
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readPm(DbPmIdRequest(readSucc.id))

        assertIs<DbPmResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readPm(DbPmIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbPmResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: InnerPmError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitPms("read") {
        override val initObjects: List<InnerPm> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = InnerPmId("ad-repo-read-notFound")
    }
}
