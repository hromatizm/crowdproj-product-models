import com.product.model.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.otus.otuskotlin.marketplace.api.v2.apiV1RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV1RequestSerialize

private const val PM_NAME = "test pm name"
private const val PM_DESCRIPTION = "test pm description"
private const val PRODUCT_GROUP_ID = "test product group id"
private const val CREATE = "create"

class V1RequestSerializationTest : FunSpec({

    val request = PmCreateRequest(
        pm = PmCreateObject(
            name = PM_NAME,
            description = PM_DESCRIPTION,
            productGroupId = PRODUCT_GROUP_ID,
        ),
        debug = PmDebug(
            mode = PmRequestDebugMode.STUB,
            stub = PmRequestDebugStubs.BAD_TITLE
        )
    )

    test("serialize successfully") {
        // Act
        val jsonString = apiV1RequestSerialize(request)

        // Assert
        val jsonObject = Json.parseToJsonElement(jsonString).jsonObject
        val pmObject = jsonObject["pm"]!!.jsonObject
        val debugObject = jsonObject["debug"]!!.jsonObject

        jsonObject["requestType"]?.jsonPrimitive?.content shouldBe CREATE
        pmObject["name"]?.jsonPrimitive?.content shouldBe PM_NAME
        pmObject["description"]?.jsonPrimitive?.content shouldBe PM_DESCRIPTION
        debugObject["mode"]?.jsonPrimitive?.content shouldBe "stub"
        debugObject["stub"]?.jsonPrimitive?.content shouldBe "badTitle"
    }

    test("deserialize successfully") {
        // Act
        val jsonString = apiV1RequestSerialize(request)
        val obj = apiV1RequestDeserialize<PmCreateRequest>(jsonString)

        // Assert
        obj shouldBe request
    }
})
