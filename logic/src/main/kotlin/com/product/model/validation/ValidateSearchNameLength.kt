package com.product.model.validation

import chain.ICorChainDsl
import chain.chain
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.validateSearchNameLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины названия для поиска. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    on { state == InnerPmState.RUNNING }
    worker("Обрезка пустых символов") { pmFilterValidating.name = pmFilterValidating.name.trim() }
    worker {
        this.title = "Проверка длины названия на 0-2 символа"
        this.description = this.title
        on { state == InnerPmState.RUNNING && pmFilterValidating.name.length in (1..2) }
        process {
            fail(
                errorValidation(
                    field = "name",
                    violationCode = "tooShort",
                    description = "Name must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка длины названия на более 100 символов"
        this.description = this.title
        on { state == InnerPmState.RUNNING && pmFilterValidating.name.length > 100 }
        process {
            fail(
                errorValidation(
                    field = "name",
                    violationCode = "tooLong",
                    description = "Name must be no more than 100 symbols long"
                )
            )
        }
    }
}
