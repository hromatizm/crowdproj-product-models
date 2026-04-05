package stub

import com.product.model.InnerPmContext
import com.product.model.inner.*
import com.product.model.processor.PmProcessor
import com.product.model.stubs.PmStub
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PmCreateStubTest : FunSpec({

    val processor = PmProcessor()
    val id = InnerPmId("test_id")
    val name = "test_name"
    val description = "test_description"
    val ownerId = "test_owner_id"

    test("create") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            stubCase = InnerPmStubs.SUCCESS,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse.id shouldBe PmStub.get().id
        ctx.pmResponse.name shouldBe name
        ctx.pmResponse.description shouldBe description
        ctx.pmResponse.ownerId.asString() shouldBe ownerId
    }

    test("bad name") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            stubCase = InnerPmStubs.BAD_NAME,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse shouldBe InnerPm()
        ctx.errors.firstOrNull()?.group shouldBe "validation"
        ctx.errors.firstOrNull()?.field shouldBe "name"
    }

    test("bad description") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            stubCase = InnerPmStubs.BAD_DESCRIPTION,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse shouldBe InnerPm()
        ctx.errors.firstOrNull()?.group shouldBe "validation"
        ctx.errors.firstOrNull()?.field shouldBe "description"
    }

    test("database error") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            stubCase = InnerPmStubs.DB_ERROR,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse shouldBe InnerPm()
        ctx.errors.firstOrNull()?.group shouldBe "internal"
        ctx.errors.firstOrNull()?.code shouldBe "internal-db"

    }

    test("bad owner id") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            stubCase = InnerPmStubs.BAD_OWNER_ID,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse shouldBe InnerPm()
        ctx.errors.firstOrNull()?.group shouldBe "validation"
        ctx.errors.firstOrNull()?.code shouldBe "validation-owner-id"
        ctx.errors.firstOrNull()?.field shouldBe "ownerId"
    }

    test("bad id") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.UPDATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            stubCase = InnerPmStubs.BAD_ID,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse shouldBe InnerPm()
        ctx.errors.firstOrNull()?.group shouldBe "validation"
        ctx.errors.firstOrNull()?.code shouldBe "validation-id"
        ctx.errors.firstOrNull()?.field shouldBe "id"
    }

    test("no snub case") {
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.STUB,
            pmRequest = InnerPm(
                id = id,
                name = name,
                description = description,
                ownerId = InnerPmUserId(ownerId)
            ),
        )

        // Act
        processor.exec(ctx)

        // Assert
        ctx.pmResponse shouldBe InnerPm()
        ctx.errors.firstOrNull()?.group shouldBe "validation"
        ctx.errors.firstOrNull()?.code shouldBe "validation"
        ctx.errors.firstOrNull()?.field shouldBe "stub"
    }
})
