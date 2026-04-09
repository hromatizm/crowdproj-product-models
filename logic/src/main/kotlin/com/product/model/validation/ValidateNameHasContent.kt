package com.product.model.validation

import chain.ICorChainDsl
import chain.worker
import com.product.model.InnerPmContext
import com.product.model.helper.errorValidation
import com.product.model.helper.fail

fun ICorChainDsl<InnerPmContext>.validateNameHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в наименовании.
        Отказываем в создании продуктовых моделей с наименованиями, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { pmValidating.name.isNotEmpty() && !pmValidating.name.contains(regExp) }
    process {
        fail(
            errorValidation(
                field = "name",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
