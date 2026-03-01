import com.product.model.InnerPmContext
import com.product.model.api.v1.models.*
import com.product.model.inner.*
import com.product.models.mappers.fromTransport
import com.product.models.mappers.toTransportPm
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
class MapperTest : FunSpec({

    test("fromTransport") {
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

        ctx.command shouldBe InnerPmCommand.CREATE
        ctx.state shouldBe InnerPmState.NONE
        ctx.errors.shouldBeEmpty()
        ctx.workMode shouldBe InnerPmWorkMode.STUB
        ctx.stubCase shouldBe InnerPmStubs.SUCCESS
        ctx.requestId shouldBe InnerPmRequestId.NONE
        ctx.timeStart shouldNotBe null
        ctx.pmRequest shouldBe expectedInnerPm
        ctx.pmFilterRequest.name shouldBe ""
        ctx.pmFilterRequest.description shouldBe ""
        ctx.pmResponse.isEmpty() shouldBe true
        ctx.pmsResponse.shouldBeEmpty()
    }

    test("toTransport") {
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

        response.result shouldBe ResponseResult.SUCCESS
        response.errors shouldContainExactly listOf(
            PmError(
                code = TEST_ERROR_CODE,
                group = TEST_ERROR_GROUP,
                field = TEST_ERROR_FIELD,
                message = TEST_ERROR_MESSAGE,
            )
        )
        response.pm?.id shouldBe TEST_PM_ID
        response.pm?.name shouldBe TEST_PM_NAME
        response.pm?.description shouldBe TEST_PM_DESCRIPTION
        response.pm?.productGroupId shouldBe TEST_PRODUCT_GROUP_ID
        response.pm?.ownerId shouldBe TEST_PM_OWNER_ID
        response.pm?.lock shouldBe TEST_PM_LOCK
        response.pm?.permissions shouldContainExactly listOf(PmPermission.READ, PmPermission.DELETE)
    }
})
