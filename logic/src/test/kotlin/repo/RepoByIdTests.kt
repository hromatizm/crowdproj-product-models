package repo

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepositoryMock
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.repo.DbPmResponseOk
import com.product.model.repo.errorNotFound
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals

private val userId = InnerPmUserId("321")
private val initPm = InnerPm(
    id = InnerPmId("123"),
    name = "abc",
    description = "abc",
    ownerId = userId,
)
private val repo = PmRepositoryMock(
    invokeReadPm = {
        if (it.id == initPm.id) {
            DbPmResponseOk(
                data = initPm,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = CorSettings(repoTest = repo)
private val processor = PmProcessor(settings)

fun repoNotFoundTest(command: InnerPmCommand) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRepo = repo,
        pmRequest = InnerPm(
            id = InnerPmId("12345"),
            name = "xyz",
            description = "xyz",
            ownerId = userId,
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(InnerPmState.FAILING, ctx.state)
    assertEquals(InnerPm(), ctx.pmResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
