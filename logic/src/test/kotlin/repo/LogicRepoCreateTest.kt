package repo

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepoInMemory
import com.product.model.PmRepositoryMock
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.repo.DbPmResponseOk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class LogicRepoCreateTest {

    private val repo = PmRepoInMemory()
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = PmProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmRequest = InnerPm(
                name = "abc",
                description = "abc",
                ownerId = InnerPmUserId("567-678-xyz-XYZ"),
                lock = InnerPmLock("123-234-abc-ABC"),
            ),
        )
        processor.exec(ctx)
        assertEquals(InnerPmState.FINISHING, ctx.state)
        assertNotEquals(InnerPmId.NONE, ctx.pmResponse.id)
        assertEquals("abc", ctx.pmResponse.name)
        assertEquals("abc", ctx.pmResponse.description)
    }
}
