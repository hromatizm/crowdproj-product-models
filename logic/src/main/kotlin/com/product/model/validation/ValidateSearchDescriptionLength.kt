package com.product.model.validation

import chain.ICorChainDsl
import chain.chain
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail
import com.product.model.inner.InnerPmState

fun ICorChainDsl<InnerPmContext>.validateSearchDescriptionLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины описания для поиска. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    on { state == InnerPmState.RUNNING }
    worker("Обрезка пустых символов") { pmFilterValidating.description = pmFilterValidating.description.trim() }
    worker {
        this.title = "Проверка длины описания на 0-2 символа"
        this.description = this.title
        on { state == InnerPmState.RUNNING && pmFilterValidating.description.length in (1..2) }
        process {
            fail(
                errorValidation(
                    field = "description",
                    violationCode = "tooShort",
                    description = "Description must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка длины описания на более 100 символов"
        this.description = this.title
        on { state == InnerPmState.RUNNING && pmFilterValidating.description.length > 100 }
        process {
            fail(
                errorValidation(
                    field = "description",
                    violationCode = "tooLong",
                    description = "Description must be no more than 100 symbols long"
                )
            )
        }
    }
}
