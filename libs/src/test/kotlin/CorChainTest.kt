import chain.CorChain
import chain.CorWorker
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CorChainTest : FunSpec({

    test(("chain should execute workers")) {
        // Arrange
        val worker1 = CorWorker<TestContext>(
            title = "Test worker 1",
            action = { history += "w1" }
        )
        val worker2 = CorWorker<TestContext>(
            title = "Test worker 1",
            action = { history += "w2" }
        )
        val chain = CorChain<TestContext>(
            title = "Test chain",
            execs = listOf(worker1, worker2)
        )
        val context = TestContext()

        // Act
        chain.exec(context)

        // Assert
        context.history shouldBe "w1w2"
    }
})