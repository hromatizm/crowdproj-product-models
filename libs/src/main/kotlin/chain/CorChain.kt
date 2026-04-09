package chain

class CorChain<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(exc: Exception) -> Unit = { throw it },
    private val execs: List<ICorExec<T>> = emptyList(),
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun process(context: T) {
        execs.forEach { it.exec(context) }
    }
}

class CorChainDsl<T> : CorExecDsl<T>(), ICorChainDsl<T> {

    var execs: List<ICorExec<T>> = mutableListOf()

    override fun add(exec: ICorExec<T>) {
        execs += exec
    }

    override fun build(): ICorExec<T> =
        CorChain(title, description, blockOn, blockExcept, execs)

}