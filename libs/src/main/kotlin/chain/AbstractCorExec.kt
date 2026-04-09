package chain

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String,
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockEcxept: suspend T.(exc: Exception) -> Unit = { throw it }
) : ICorExec<T> {

    override suspend fun exec(context: T) {
        try {
            if (blockOn(context)) {
                process(context)
            }
        } catch (exc: Exception) {
            blockEcxept(context, exc)
        }
    }

    protected abstract suspend fun process(context: T)
}

abstract class CorExecDsl<T> : ICorExecDsl<T> {
    override var title: String = ""
    override var description: String = ""
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(exc: Exception) -> Unit = { throw it }

    override fun on(block: suspend T.() -> Boolean) {
        blockOn = block
    }

    override fun except(block: suspend T.(exc: Exception) -> Unit) {
        blockExcept = block
    }
}