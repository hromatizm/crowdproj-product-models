package com.product.model

import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmUserId
import com.product.model.repo.DbPmRequest
import com.product.model.repo.DbPmResponseOk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals


abstract class RepoPmCreateTest {
    abstract val repo: IRepoPmInitializable
    protected open val uuidNew = InnerPmId("10000000-0000-0000-0000-000000000001")

    private val createObj = InnerPm(
        name = "create object",
        description = "create object description",
        ownerId = InnerPmUserId("owner-123"),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createPm(DbPmRequest(createObj))
        val expected = createObj
        assertIs<DbPmResponseOk>(result)
        assertNotEquals(InnerPmId.NONE, result.data.id)
        assertEquals(uuidNew.asString(), result.data.lock.asString())
        assertEquals(expected.name, result.data.name)
        assertEquals(expected.description, result.data.description)
        assertNotEquals(InnerPmId.NONE, result.data.id)
    }

    companion object : BaseInitPms("create") {
        override val initObjects: List<InnerPm> = emptyList()
    }
}
