package repo

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepositoryMock
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.repo.DbPmsResponseOk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LogicRepoSearchTest {

    private val userId = InnerPmUserId("321")
    private val command = InnerPmCommand.SEARCH
    private val initPm = InnerPm(
        id = InnerPmId("123"),
        name = "abc",
        description = "abc",
        ownerId = userId,
    )
    private val repo = PmRepositoryMock(
        invokeSearchPm = {
            DbPmsResponseOk(
                data = listOf(initPm),
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = PmProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = InnerPmContext(
            command = command,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmRepo = repo,
            pmFilterRequest = InnerPmFilter(
                name = "abc",
                description = "abc"
            ),
        )
        processor.exec(ctx)
        assertEquals(InnerPmState.FINISHING, ctx.state)
        assertEquals(1, ctx.pmsResponse.size)
    }
}
