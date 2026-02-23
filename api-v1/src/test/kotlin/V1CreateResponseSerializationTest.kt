package com.product.model.api.v1

import apiV1Mapper
import apiV1ResponseDeserialize
import apiV1ResponseSerialize
import com.product.model.api.v1.models.PmCreateResponse
import com.product.model.api.v1.models.PmPermission
import com.product.model.api.v1.models.PmResponseObject
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

private const val PM_NAME = "test pm name"
private const val PM_DESCRIPTION = "test pm description"
private const val PRODUCT_GROUP_ID = "test product group id"
private const val CREATE = "create"
private const val PM_ID = "test id"
private const val PM_OWNER_ID = "test owner id"
private const val LOCK = "test lock"

class V1ResponseSerializationTest {

    private val permissions = setOf(PmPermission.READ, PmPermission.UPDATE, PmPermission.DELETE)

    private val response = PmCreateResponse(
        responseType = CREATE,
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

    @Test
    fun `serialize successfully`() {
        val jsonString = apiV1ResponseSerialize(response)

        val jsonNode = apiV1Mapper.readTree(jsonString)

        assertThat(jsonNode.get("responseType").asText()).isEqualTo(CREATE)
        assertThat(jsonNode.get("pm").get("description").asText()).isEqualTo(PM_DESCRIPTION)
        assertThat(jsonNode.get("pm").get("id").asText()).isEqualTo(PM_ID)
        assertThat(jsonNode.get("pm").get("lock").asText()).isEqualTo(LOCK)
        assertThat(jsonNode.get("pm").get("name").asText()).isEqualTo(PM_NAME)
        assertThat(jsonNode.get("pm").get("ownerId").asText()).isEqualTo(PM_OWNER_ID)
        assertThat(jsonNode.get("pm").get("permissions").asIterable().map { it.asText() })
            .containsExactlyElementsOf(permissions.map { it.value })
        assertThat(jsonNode.get("pm").get("productGroupId").asText()).isEqualTo(PRODUCT_GROUP_ID)
    }

    @Test
    fun `deserialize successfully`() {
        val jsonString = apiV1ResponseSerialize(response)
        val obj = apiV1ResponseDeserialize(jsonString) as PmCreateResponse

        assertThat(obj).isEqualTo(response)
    }
}