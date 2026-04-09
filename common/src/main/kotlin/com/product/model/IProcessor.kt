package com.product.model

interface IProcessor {

    suspend fun exec(ctx: InnerPmContext)
}