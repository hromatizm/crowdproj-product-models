package chain

class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(exc: Exception) -> Unit = { throw it },
    private val action: suspend T.() -> Unit,
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun process(context: T) {
        action(context)
    }
}

class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {

    var action: suspend T.() -> Unit = {}

    override fun process(block: suspend T.() -> Unit) {
        action = block
    }

    override fun build(): ICorExec<T> =
        CorWorker(title, description, blockOn, blockExcept, action)
}