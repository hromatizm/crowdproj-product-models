package validation

import com.product.model.CorSettings
import com.product.model.InnerPmContext
import com.product.model.PmRepoInMemory
import com.product.model.inner.InnerPmCommand
import com.product.model.inner.InnerPmFilter
import com.product.model.inner.InnerPmState
import com.product.model.inner.InnerPmWorkMode
import com.product.model.processor.PmProcessor
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotEquals

class LogicValidationSearchTest : BaseLogicValidationTest() {
    override val command = InnerPmCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val settings = CorSettings(
            repoTest = PmRepoInMemory(),
        )
        val processor = PmProcessor(settings)
        val ctx = InnerPmContext(
            command = command,
            state = InnerPmState.NONE,
            workMode = InnerPmWorkMode.TEST,
            pmFilterRequest = InnerPmFilter(),
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(InnerPmState.FAILING, ctx.state)
    }
}
