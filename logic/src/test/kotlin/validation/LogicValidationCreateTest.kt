package validation

import com.product.model.inner.InnerPmCommand
import org.junit.jupiter.api.Test

// смотрим пример теста валидации, собранного из тестовых функций-оберток
class LogicValidationCreateTest : BaseLogicValidationTest() {
    override val command: InnerPmCommand = InnerPmCommand.CREATE

    @Test
    fun correctTitle() = validationNameCorrect(command, processor)
    @Test
    fun trimTitle() = validationNameTrim(command, processor)
    @Test
    fun emptyTitle() = validationNameEmpty(command, processor)
    @Test
    fun badSymbolsTitle() = validationNameSymbols(command, processor)

    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test
    fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test
    fun correctOwnerId() = validationOwnerIdCorrect(command, processor)
    @Test
    fun trimOwnerId() = validationOwnerIdTrim(command, processor)
    @Test
    fun emptyOwnerId() = validationOwnerIdEmpty(command, processor)
    @Test
    fun badSymbolsOwnerId() = validationOwnerIdFormat(command, processor)
}
