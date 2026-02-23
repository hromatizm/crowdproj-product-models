import com.product.model.InnerPmContext
import com.product.model.api.v1.models.*
import com.product.model.inner.*
import com.product.models.mappers.fromTransport
import com.product.models.mappers.toTransportPm
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TEST_PM_NAME = "test pm name"
private const val TEST_PM_DESCRIPTION = "test pm description"
private const val TEST_PRODUCT_GROUP_ID = "test product group id"
private const val TEST_PM_ID = "test pm id"
private const val TEST_PM_REQUEST_ID = "test pm request id"
private const val TEST_PM_OWNER_ID = "test pm owner id"
private const val TEST_PM_LOCK = "test pm lock"
private const val TEST_ERROR_CODE = "test error code"
private const val TEST_ERROR_GROUP = "test error group"
private const val TEST_ERROR_FIELD = "test error field"
private const val TEST_ERROR_MESSAGE = "test error message"

@OptIn(ExperimentalTime::class)
class MapperTest {

    @Test
    fun fromTransport() {
        val req = PmCreateRequest(
            debug = PmDebug(
                mode = PmRequestDebugMode.STUB,
                stub = PmRequestDebugStubs.SUCCESS,
            ),
            pm = PmCreateObject(
                name = TEST_PM_NAME,
                description = TEST_PM_DESCRIPTION,
                productGroupId = TEST_PRODUCT_GROUP_ID,
            )
        )
        val expectedInnerPm = InnerPm(
            id = InnerPmId.NONE,
            name = TEST_PM_NAME,
            description = TEST_PM_DESCRIPTION,
            productGroupId = InnerPmProductGroupId(TEST_PRODUCT_GROUP_ID),
            ownerId = InnerPmUserId.NONE,
            lock = InnerPmLock.NONE,
            permissions = mutableSetOf(),
        )

        val ctx = InnerPmContext()
        ctx.fromTransport(req)

        assertThat(ctx.command).isEqualTo(InnerPmCommand.CREATE)
        assertThat(ctx.state).isEqualTo(InnerPmState.NONE)
        assertThat(ctx.errors).isEmpty()
        assertThat(ctx.workMode).isEqualTo(InnerPmWorkMode.STUB)
        assertThat(ctx.stubCase).isEqualTo(InnerPmStubs.SUCCESS)
        assertThat(ctx.requestId).isEqualTo(InnerPmRequestId.NONE)
        assertThat(ctx.timeStart).isNotNull()
        assertThat(ctx.pmRequest).isEqualTo(expectedInnerPm)
        assertThat(ctx.pmFilterRequest.name).isEqualTo("")
        assertThat(ctx.pmFilterRequest.description).isEqualTo("")
        assertThat(ctx.pmResponse.isEmpty()).isTrue()
        assertThat(ctx.pmsResponse).isEmpty()
    }

    @Test
    fun toTransport() {
        val now = Clock.System.now()
        val ctx = InnerPmContext(
            command = InnerPmCommand.CREATE,
            state = InnerPmState.RUNNING,
            errors = mutableListOf(
                InnerPmError(
                    code = TEST_ERROR_CODE,
                    group = TEST_ERROR_GROUP,
                    field = TEST_ERROR_FIELD,
                    message = TEST_ERROR_MESSAGE,
                    exception = Exception("test exception"),
                    level = InnerPmError.LogLevel.ERROR,
                )
            ),
            workMode = InnerPmWorkMode.PROD,
            stubCase = InnerPmStubs.NONE,
            requestId = InnerPmRequestId(TEST_PM_REQUEST_ID),
            timeStart = now,
            pmRequest = InnerPm(
                id = InnerPmId(TEST_PM_ID),
                name = TEST_PM_NAME,
                description = TEST_PM_DESCRIPTION,
                productGroupId = InnerPmProductGroupId(TEST_PRODUCT_GROUP_ID),
                ownerId = InnerPmUserId(TEST_PM_OWNER_ID),
                lock = InnerPmLock(TEST_PM_LOCK),
                permissions = mutableSetOf(InnerPmPermission.READ, InnerPmPermission.DELETE),
            ),
            pmResponse = InnerPm(
                id = InnerPmId(TEST_PM_ID),
                name = TEST_PM_NAME,
                description = TEST_PM_DESCRIPTION,
                productGroupId = InnerPmProductGroupId(TEST_PRODUCT_GROUP_ID),
                ownerId = InnerPmUserId(TEST_PM_OWNER_ID),
                lock = InnerPmLock(TEST_PM_LOCK),
                permissions = mutableSetOf(InnerPmPermission.READ, InnerPmPermission.DELETE),
            ),
            pmsResponse = mutableListOf()
        )

        val response = ctx.toTransportPm() as PmCreateResponse

        assertThat(response.result).isEqualTo(ResponseResult.SUCCESS)
        assertThat(response.errors).containsExactly(
            PmError(
                code = TEST_ERROR_CODE,
                group = TEST_ERROR_GROUP,
                field = TEST_ERROR_FIELD,
                message = TEST_ERROR_MESSAGE,
            )
        )
        assertThat(response.pm?.id).isEqualTo(TEST_PM_ID)
        assertThat(response.pm?.name).isEqualTo(TEST_PM_NAME)
        assertThat(response.pm?.description).isEqualTo(TEST_PM_DESCRIPTION)
        assertThat(response.pm?.productGroupId).isEqualTo(TEST_PRODUCT_GROUP_ID)
        assertThat(response.pm?.ownerId).isEqualTo(TEST_PM_OWNER_ID)
        assertThat(response.pm?.lock).isEqualTo(TEST_PM_LOCK)
        assertThat(response.pm?.permissions).containsExactly(PmPermission.READ, PmPermission.DELETE)
    }
}
