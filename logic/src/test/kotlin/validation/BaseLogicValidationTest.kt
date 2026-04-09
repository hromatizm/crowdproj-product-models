package validation

import com.product.model.CorSettings
import com.product.model.processor.PmProcessor
import com.product.model.inner.InnerPmCommand

abstract class BaseLogicValidationTest {
    protected abstract val command: InnerPmCommand
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { PmProcessor(settings) }
}
