package com.product.model.helper

import com.product.model.AppSettings
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmCommand
import com.product.model.inner.InnerPmState
import com.product.model.toLog
import kotlin.reflect.KClass
import kotlin.time.Clock

suspend inline fun <T> AppSettings.controllerHelper(
    crossinline getRequest: suspend InnerPmContext.() -> Unit,
    crossinline toResponse: suspend InnerPmContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = InnerPmContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = InnerPmState.FAILING
        ctx.errors.add(e.asPmError())
        processor.exec(ctx)
        if (ctx.command == InnerPmCommand.NONE) {
            ctx.command = InnerPmCommand.READ
        }
        ctx.toResponse()
    }
}
