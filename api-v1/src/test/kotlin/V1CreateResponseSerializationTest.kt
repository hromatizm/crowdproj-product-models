import com.product.model.api.v1.models.PmCreateResponse
import com.product.model.api.v1.models.PmPermission
import com.product.model.api.v1.models.PmResponseObject
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.otus.otuskotlin.marketplace.api.v2.apiV1ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV1ResponseSerialize

private const val PM_NAME = "test pm name"
private const val PM_DESCRIPTION = "test pm description"
private const val PRODUCT_GROUP_ID = "test product group id"
private const val CREATE = "create"
private const val PM_ID = "test id"
private const val PM_OWNER_ID = "test owner id"
private const val LOCK = "test lock"

class V1ResponseSerializationTest : FunSpec({

    val permissions = setOf(PmPermission.READ, PmPermission.UPDATE, PmPermission.DELETE)

    val response = PmCreateResponse(
        pm = PmResponseObject(
            id = PM_ID,
            name = PM_NAME,
            description = PM_DESCRIPTION,
            productGroupId = PRODUCT_GROUP_ID,
            ownerId = PM_OWNER_ID,
            permissions = permissions,
            lock = LOCK,
        ),
    )

    test("serialize successfully") {
        // Act
        val jsonString = apiV1ResponseSerialize(response)

        // Assert
        val jsonObject = Json.parseToJsonElement(jsonString).jsonObject
        val pmObject = jsonObject["pm"]!!.jsonObject

        jsonObject["responseType"]?.jsonPrimitive?.content shouldBe CREATE
        pmObject["description"]?.jsonPrimitive?.content shouldBe PM_DESCRIPTION
        pmObject["id"]?.jsonPrimitive?.content shouldBe PM_ID
        pmObject["lock"]?.jsonPrimitive?.content shouldBe LOCK
        pmObject["name"]?.jsonPrimitive?.content shouldBe PM_NAME
        pmObject["ownerId"]?.jsonPrimitive?.content shouldBe PM_OWNER_ID
        pmObject["productGroupId"]?.jsonPrimitive?.content shouldBe PRODUCT_GROUP_ID
        pmObject["permissions"]?.jsonArray
            ?.map { it.jsonPrimitive.content }
            ?.shouldContainExactlyInAnyOrder(permissions.map { it.value })
    }

    test("deserialize successfully") {
        // Act
        val jsonString = apiV1ResponseSerialize(response)
        val obj = apiV1ResponseDeserialize<PmCreateResponse>(jsonString)

        // Assert
        obj shouldBe response
    }
})
