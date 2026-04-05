package validation

import com.product.model.InnerPmContext
import com.product.model.processor.PmProcessor
import com.product.model.inner.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.assertContains

fun validationIdCorrect(command: InnerPmCommand, processor: PmProcessor) = runTest {
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

fun validationIdTrim(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId(" \n\t 123-234-abc-ABC \n\t "),
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

fun validationIdEmpty(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId(""),
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("123-234-abc-ABC"),
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = InnerPm(
            id = InnerPmId("!@#\$%^&*(),.{}"),
            name = "abc",
            description = "abc",
            ownerId = InnerPmUserId("123-234-abc-ABC"),
            lock = InnerPmLock("123-234-abc-ABC"),
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
