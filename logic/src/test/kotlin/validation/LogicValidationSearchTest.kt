package validation

import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmCommand
import com.product.model.inner.InnerPmFilter
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmWorkMode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotEquals

class LogicValidationSearchTest : BaseLogicValidationTest() {
    override val command = InnerPmCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = InnerPmContext(
            command = command,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmFilterRequest = InnerPmFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(InnerPmState.FAILING, ctx.state)
    }
}
