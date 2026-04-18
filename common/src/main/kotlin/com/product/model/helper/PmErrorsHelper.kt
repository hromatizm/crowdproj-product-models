package com.product.model.helper

import com.product.model.InnerPmContext
import com.product.model.LogLevel
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmState

fun Throwable.asPmError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = InnerPmError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun InnerPmContext.addError(vararg error: InnerPmError) = errors.addAll(error)

fun InnerPmContext.fail(error: InnerPmError) {
    addError(error)
    state = InnerPmState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = InnerPmError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = InnerPmError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)

