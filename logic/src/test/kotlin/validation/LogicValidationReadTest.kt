package validation

import com.product.model.inner.InnerPmCommand
import kotlin.test.Test

class LogicValidationReadTest : BaseLogicValidationTest() {
    override val command = InnerPmCommand.READ

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)

}
