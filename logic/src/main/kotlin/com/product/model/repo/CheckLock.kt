package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == InnerPmState.RUNNING && pmValidated.lock != pmRepoRead.lock }
    process {
        fail(errorRepoConcurrency(pmRepoRead, pmValidated.lock).errors)
    }
}