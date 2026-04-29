package validation

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepoInMemory
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.assertContains

fun validationOwnerIdCorrect(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = InnerPmId("123-234-abc-ABC"),
        name = "abc",
        description = "abc",
        ownerId = InnerPmUserId("123-234-abc-ABC"),
        lock = InnerPmLock("123-234-abc-ABC"),
    )
    val settings = CorSettings(
        repoTest = PmRepoInMemory().apply { save(listOf(pm)) },
    )
    val processor = PmProcessor(settings)
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = pm,
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerPmState.FAILING, ctx.state)
}

fun validationOwnerIdTrim(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = InnerPmId("123-234-abc-ABC"),
        name = "abc",
        description = "abc",
        ownerId = InnerPmUserId(" \n\t 123-234-abc-ABC \n\t "),
        lock = InnerPmLock("123-234-abc-ABC"),
    )
    val settings = CorSettings(
        repoTest = PmRepoInMemory().apply { save(listOf(pm)) },
    )
    val processor = PmProcessor(settings)
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = pm,
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerPmState.FAILING, ctx.state)
}

fun validationOwnerIdEmpty(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = InnerPmId("123-234-abc-ABC"),
        name = "abc",
        description = "abc",
        ownerId = InnerPmUserId(""),
        lock = InnerPmLock("123-234-abc-ABC"),
    )
    val settings = CorSettings(
        repoTest = PmRepoInMemory().apply { save(listOf(pm)) },
    )
    val processor = PmProcessor(settings)
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = pm
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ownerId", error?.field)
    assertContains(error?.message ?: "", "ownerId")
}

fun validationOwnerIdFormat(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = InnerPmId("123-234-abc-ABC"),
        name = "abc",
        description = "abc",
        ownerId = InnerPmUserId("!@#\$%^&*(),.{}"),
        lock = InnerPmLock("123-234-abc-ABC"),
    )
    val settings = CorSettings(
        repoTest = PmRepoInMemory().apply { save(listOf(pm)) },
    )
    val processor = PmProcessor(settings)
    val ctx = InnerPmContext(
        command = command,
        state = InnerPmState.NONE,
        workMode = InnerPmWorkMode.TEST,
        pmRequest = pm,
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(InnerPmState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ownerId", error?.field)
    assertContains(error?.message ?: "", "ownerId")
}
