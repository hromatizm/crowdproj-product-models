import chain.CorWorker
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CorWorkerTest : FunSpec({

    test(("worker should execute action")) {
        // Arrange
        val worker = CorWorker<TestContext>(
            title = "Test worker",
            action = { history += "w1" }
        )
        val context = TestContext()

        // Act
        worker.exec(context)

        // Assert
        context.history shouldBe "w1"
    }

    test("worker should not execute action if on is false") {
        val worker = CorWorker<TestContext>(
            title = "Test worker",
            blockOn = { status == TestStatus.ERROR },
            action = { history += "w1" }
        )
        val context = TestContext()

        // Act
        worker.exec(context)

        // Assert
        context.history shouldBe ""
    }

    test("worker should handle exception") {
        val worker = CorWorker<TestContext>(
            title = "Test worker",
            action = { throw RuntimeException("Test exception") },
            blockExcept = { exc -> history += exc.message }
        )
        val context = TestContext()

        // Act
        worker.exec(context)

        // Assert
        context.history shouldBe "Test exception"
    }
})