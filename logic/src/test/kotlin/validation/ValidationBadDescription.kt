package validation

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepoInMemory
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.stubs.PmStub
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.assertContains

private val stub = PmStub.get()

fun validationDescriptionCorrect(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = stub.id,
        name = "abc",
        ownerId = InnerPmUserId("123-234-abc-ABC"),
        description = "abc",
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
    assertEquals("abc", ctx.pmValidated.description)
}

fun validationDescriptionTrim(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = stub.id,
        name = "abc",
        ownerId = InnerPmUserId("123-234-abc-ABC"),
        description = " \n\tabc \n\t",
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
    assertEquals("abc", ctx.pmValidated.description)
}

fun validationDescriptionEmpty(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = stub.id,
        name = "abc",
        ownerId = InnerPmUserId("123-234-abc-ABC"),
        description = "",
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
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: InnerPmCommand, processor: PmProcessor) = runTest {
    val pm = InnerPm(
        id = stub.id,
        name = "abc",
        ownerId = InnerPmUserId("123-234-abc-ABC"),
        description = "!@#$%^&*(),.{}",
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
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
