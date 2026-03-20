package com.product.model.logback

import ch.qos.logback.classic.Logger
import com.product.model.IPmLogWrapper
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.logging.jvm.PmLogWrapperLogback
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun pmLoggerLogback(logger: Logger): IPmLogWrapper = PmLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun pmLoggerLogback(clazz: KClass<*>): IPmLogWrapper = pmLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun pmLoggerLogback(loggerId: String): IPmLogWrapper = pmLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
