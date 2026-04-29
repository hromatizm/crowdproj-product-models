package repo

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepositoryMock
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.repo.DbPmResponseErr
import com.product.model.repo.DbPmResponseOk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogicRepoDeleteTest {

    private val userId = InnerPmUserId("321")
    private val command = InnerPmCommand.DELETE
    private val initPm = InnerPm(
        id = InnerPmId("123"),
        name = "abc",
        description = "abc",
        ownerId = userId,
        lock = InnerPmLock("123-234-abc-ABC"),
    )
    private val repo = PmRepositoryMock(
        invokeReadPm = {
            DbPmResponseOk(
                data = initPm,
            )
        },
        invokeDeletePm = {
            if (it.id == initPm.id)
                DbPmResponseOk(
                    data = initPm
                )
            else DbPmResponseErr()
        }
    )
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor = PmProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = InnerPm(
            id = InnerPmId("123"),
            lock = InnerPmLock("123-234-abc-ABC"),
        )
        val ctx = InnerPmContext(
            command = command,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(InnerPmState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initPm.id, ctx.pmResponse.id)
        assertEquals(initPm.name, ctx.pmResponse.name)
        assertEquals(initPm.description, ctx.pmResponse.description)

    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
