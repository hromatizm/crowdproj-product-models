package validation

import com.product.model.InnerPmContext
import com.product.model.PmProcessor
import com.product.model.inner.*
import com.product.model.stubs.PmStub
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.assertContains

private val stub = PmStub.get()

fun validationNameCorrect(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = stub.id,
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("567-678-xyz-XYZ"),
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerPmState.FAILING, ctx.state)
    assertEquals("abc", ctx.pmValidated.name)
}

fun validationNameTrim(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = stub.id,
            name = " \n\t abc \t\n ",
            description = "abc",
            ownerId = InnerPmUserId("567-678-xyz-XYZ"),
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerPmState.FAILING, ctx.state)
    assertEquals("abc", ctx.pmValidated.name)
}

fun validationNameEmpty(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = stub.id,
            name = "",
            description = "abc",
            ownerId = InnerPmUserId("567-678-xyz-XYZ"),
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}

fun validationNameSymbols(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId("123"),
            name = "!@#$%^&*(),.{}",
            ownerId = InnerPmUserId("567-678-xyz-XYZ"),
            description = "abc",
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("name", error?.field)
    assertContains(error?.message ?: "", "name")
}
