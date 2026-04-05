package validation

import com.product.model.InnerPmContext
import com.product.model.PmProcessor
import com.product.model.inner.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.assertContains

fun validationLockCorrect(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("123-234-abc-ABC"),
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerPmState.FAILING, ctx.state)
}

fun validationLockTrim(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("123-234-abc-ABC"),
            lock = InnerPmLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerPmState.FAILING, ctx.state)
}

fun validationLockEmpty(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("123-234-abc-ABC"),
            lock = InnerPmLock(""),
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId("123-234-abc-ABC"),
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("123-234-abc-ABC"),
            lock = InnerPmLock("!@#\$%^&*(),.{}"),
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
