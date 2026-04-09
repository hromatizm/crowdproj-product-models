import chain.DslGenericGuard

class TestContext(
    var history: String = "",
    var status: TestStatus = TestStatus.NONE
) : DslGenericGuard

enum class TestStatus {
    NONE,
    RUNNING,
    ERROR,
}