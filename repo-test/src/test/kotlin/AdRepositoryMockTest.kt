import com.product.model.PmRepositoryMock
import com.product.model.inner.InnerPm
import com.product.model.repo.*
import com.product.model.stubs.PmStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PmRepositoryMockTest {
    private val repo = PmRepositoryMock(
        invokeCreatePm = { DbPmResponseOk(PmStub.prepareResult { name = "create" }) },
        invokeReadPm = { DbPmResponseOk(PmStub.prepareResult { name = "read" }) },
        invokeUpdatePm = { DbPmResponseOk(PmStub.prepareResult { name = "update" }) },
        invokeDeletePm = { DbPmResponseOk(PmStub.prepareResult { name = "delete" }) },
        invokeSearchPm = { DbPmsResponseOk(listOf(PmStub.prepareResult { name = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createPm(DbPmRequest(InnerPm()))
        assertIs<DbPmResponseOk>(result)
        assertEquals("create", result.data.name)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readPm(DbPmIdRequest(InnerPm()))
        assertIs<DbPmResponseOk>(result)
        assertEquals("read", result.data.name)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updatePm(DbPmRequest(InnerPm()))
        assertIs<DbPmResponseOk>(result)
        assertEquals("update", result.data.name)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deletePm(DbPmIdRequest(InnerPm()))
        assertIs<DbPmResponseOk>(result)
        assertEquals("delete", result.data.name)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchPm(DbPmFilterRequest())
        assertIs<DbPmsResponseOk>(result)
        assertEquals("search", result.data.first().name)
    }

}
