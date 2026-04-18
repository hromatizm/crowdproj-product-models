package com.product.model.repo

import com.product.model.helper.errorSystem
import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmError
import com.product.model.inner.InnerPmId
import com.product.model.inner.InnerPmLock
import com.product.model.repo.exceptions.RepoConcurrencyException
import com.product.model.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: InnerPmId) = DbPmResponseErr(
    InnerPmError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbPmResponseErr(
    InnerPmError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldPm: InnerPm,
    expectedLock: InnerPmLock,
    exception: Exception = RepoConcurrencyException(
        id = oldPm.id,
        expectedLock = expectedLock,
        actualLock = oldPm.lock,
    ),
) = DbPmResponseErrWithData(
    ad = oldPm,
    err = InnerPmError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldPm.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: InnerPmId) = DbPmResponseErr(
    InnerPmError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbPmResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)