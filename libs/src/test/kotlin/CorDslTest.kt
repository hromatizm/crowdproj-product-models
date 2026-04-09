import chain.chain
import chain.rootChain
import chain.worker
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CorDslTest : FunSpec({

    test(("worker should execute action")) {
        val chain = rootChain<TestContext> {
            worker {
                process { history += "w1" }
            }
        }
        val context = TestContext()

        // Act
        chain.build().exec(context)

        // Assert
        context.history shouldBe "w1"
    }

    test("worker should not execute action if on is false") {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestStatus.ERROR }
                process { history += "w1" }
            }
        }
        val context = TestContext()

        // Act
        chain.build().exec(context)

        // Assert
        context.history shouldBe ""
    }

    test("worker should handle exception") {
        val chain = rootChain<TestContext> {
            worker {
                process { throw RuntimeException("Test exception") }
                except { history += it.message }
            }
        }
        val context = TestContext()

        // Act
        chain.build().exec(context)

        // Assert
        context.history shouldBe "Test exception"
    }

    test("should throw when exception and no except") {
        val chain = rootChain<TestContext> {
            worker("throw", "Выбрасывает ошибку") { throw RuntimeException("Test exception") }
        }
        val context = TestContext()

        // Act & Assert
        shouldThrow<RuntimeException> { chain.build().exec(context) }
    }

    test("inner chain") {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestStatus.NONE }
                process { status = TestStatus.RUNNING }
                except { status = TestStatus.ERROR }
            }
            chain {
                worker("w3", "Worker 3") {
                }
                on { status == TestStatus.RUNNING }
                worker {
                    on { status == TestStatus.RUNNING }
                    process { history += "w2" }
                }
            }
        }
        val context = TestContext()

        // Act
        chain.build().exec(context)

        // Assert
        context.history shouldBe "w2"
    }
})