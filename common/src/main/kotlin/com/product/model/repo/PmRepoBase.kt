package com.product.model.repo

import com.product.model.helper.errorSystem

abstract class PmRepoBase: IRepoPm {

    protected suspend fun tryPmMethod(block: suspend () -> IDbPmResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPmResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryPmsMethod(block: suspend () -> IDbPmsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbPmsResponseErr(errorSystem("methodException", e = e))
    }

}
