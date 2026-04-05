package validation

import chain.rootChain
import com.product.model.InnerPmContext
import com.product.model.inner.InnerPm
import com.product.model.inner.InnerPmFilter
import com.product.model.inner.InnerPmState
import com.product.model.validation.validateNameHasContent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidateNameHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmValidating = InnerPm(name = "")
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmValidating = InnerPm(name = "12!@#$%^&*()_+-=")
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-name-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = InnerPmContext(
            state = InnerPmState.RUNNING,
            pmFilterValidating = InnerPmFilter(name = "Ж")
        )

        chain.exec(ctx)

        assertEquals(InnerPmState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateNameHasContent("")
        }.build()
    }
}
