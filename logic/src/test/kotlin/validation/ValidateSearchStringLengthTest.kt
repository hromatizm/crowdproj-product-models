package validation

import chain.rootChain
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPmFilter
import com.product.model.inner.InnerPmState
import com.product.model.validation.validateSearchNameLength
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidateSearchNameLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmFilterValidating = InnerPmFilter(name = "")
        )
        chain.exec(ctx)
        assertEquals(InnerPmState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmFilterValidating = InnerPmFilter(name = "  ")
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmFilterValidating = InnerPmFilter(name = "12")
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmFilterValidating = InnerPmFilter(name = "123")
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmFilterValidating = InnerPmFilter(name = "12".repeat(51))
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchNameLength("")
        }.build()
    }
}
