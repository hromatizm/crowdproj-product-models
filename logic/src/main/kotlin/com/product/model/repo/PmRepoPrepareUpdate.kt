package com.product.model.repo

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == InnerPmState.RUNNING }
    process {
        pmRepoPrepare = pmRepoRead.deepCopy().apply {
            this.name = pmValidated.name
            description = pmValidated.description
            lock = pmValidated.lock
        }
    }
}
