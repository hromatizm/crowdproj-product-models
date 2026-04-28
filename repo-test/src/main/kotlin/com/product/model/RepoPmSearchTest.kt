package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmUserId
import com.product.model.repo.DbPmFilterRequest
import com.product.model.repo.DbPmsResponseOk
import com.product.model.repo.IRepoPm
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPmSearchTest {
    abstract val repo: IRepoPm

    protected open val initializedObjects: List<InnerPm> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchPm(DbPmFilterRequest(ownerId = searchOwnerId))
        assertIs<DbPmsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object : BaseInitPms("search") {

        val searchOwnerId = InnerPmUserId("owner-124")
        override val initObjects: List<InnerPm> = listOf(
            createInitTestModel("pm1"),
            createInitTestModel("pm2", ownerId = searchOwnerId),
            createInitTestModel("pm3", ownerId = searchOwnerId),
        )
    }
}
