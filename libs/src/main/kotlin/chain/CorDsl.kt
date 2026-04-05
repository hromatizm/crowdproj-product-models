package chain

@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String

    fun on(block: suspend T.() -> Boolean)

    fun except(block: suspend T.(exc: Exception) -> Unit)

    fun build(): ICorExec<T>
}

interface ICorWorkerDsl<T> : ICorExecDsl<T> {
    fun process(block: suspend T.() -> Unit)
}

interface ICorChainDsl<T> : ICorExecDsl<T> {
    fun add(exec: ICorExec<T>)
}

fun <T> rootChain(block: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> =
    CorChainDsl<T>().apply { block() }

fun <T> ICorChainDsl<T>.worker(block: ICorWorkerDsl<T>.() -> Unit) {
    add(
        CorWorkerDsl<T>().apply(block).build()
    )
}

fun <T> ICorChainDsl<T>.chain(block: ICorChainDsl<T>.() -> Unit) {
    add(
        CorChainDsl<T>().apply(block).build()
    )
}

fun <T> ICorChainDsl<T>.worker(title: String, description: String = "", blockHandle: suspend T.() -> Unit) {
    worker {
        this.title = title
        this.description = description
        process(blockHandle)
    }
}
