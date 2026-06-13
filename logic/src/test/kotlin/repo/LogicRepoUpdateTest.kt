package repo

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepositoryMock
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.repo.DbPmResponseOk
import com.product.model.repo.DbPmsResponseOk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LogicRepoUpdateTest {

    private val userId = InnerPmUserId("321")
    private val command = InnerPmCommand.UPDATE
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
        invokeUpdatePm = {
            DbPmResponseOk(
                data = InnerPm(
                    id = InnerPmId("123"),
                    name = "xyz",
                    description = "xyz",
                    lock = InnerPmLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = PmProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val pmToUpdate = InnerPm(
            id = InnerPmId("123"),
            name = "xyz",
            description = "xyz",
            ownerId = userId,
            lock = InnerPmLock("123-234-abc-ABC"),
        )
        val ctx = InnerPmContext(
            command = command,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmRequest = pmToUpdate,
        )
        processor.exec(ctx)
        assertEquals(InnerPmState.FINISHING, ctx.state)
        assertEquals(pmToUpdate.id, ctx.pmResponse.id)
        assertEquals(pmToUpdate.name, ctx.pmResponse.name)
        assertEquals(pmToUpdate.description, ctx.pmResponse.description)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
