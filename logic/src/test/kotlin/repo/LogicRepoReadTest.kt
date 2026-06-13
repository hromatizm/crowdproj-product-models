package repo

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepositoryMock
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.repo.DbPmResponseOk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LogicRepoReadTest {

    private val userId = InnerPmUserId("321")
    private val command = InnerPmCommand.READ
    private val initPm = InnerPm(
        id = InnerPmId("123"),
        name = "abc",
        description = "abc",
        ownerId = userId,
    )
    private val repo = PmRepositoryMock(
        invokeReadPm = {
            DbPmResponseOk(
                data = initPm,
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = PmProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = InnerPmContext(
            command = command,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmRequest = InnerPm(
                id = InnerPmId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(InnerPmState.FINISHING, ctx.state)
        assertEquals(initPm.id, ctx.pmResponse.id)
        assertEquals(initPm.name, ctx.pmResponse.name)
        assertEquals(initPm.description, ctx.pmResponse.description)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
