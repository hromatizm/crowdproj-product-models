package com.product.model.api.v1

import apiV1Mapper
import apiV1RequestDeserialize
import apiV1RequestSerialize
import com.product.model.api.v1.models.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

private const val PM_NAME = "test pm name"
private const val PM_DESCRIPTION = "test pm description"
private const val PRODUCT_GROUP_ID = "test product group id"
private const val CREATE = "create"

class V1RequestSerializationTest {

    private val request = PmCreateRequest(
        requestType = CREATE,
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

    @Test
    fun `serialize successfully`() {
        val jsonString = apiV1RequestSerialize(request)

        val jsonNode = apiV1Mapper.readTree(jsonString)

        assertThat(jsonNode.get("requestType").asText()).isEqualTo(CREATE)
        assertThat(jsonNode.get("pm").get("name").asText()).isEqualTo(PM_NAME)
        assertThat(jsonNode.get("pm").get("description").asText()).isEqualTo(PM_DESCRIPTION)

        assertThat(jsonNode.get("debug").get("mode").asText()).isEqualTo("stub")
        assertThat(jsonNode.get("debug").get("stub").asText()).isEqualTo("badTitle")
    }

    @Test
    fun `deserialize successfully`() {
        val jsonString = apiV1RequestSerialize(request)
        val obj = apiV1RequestDeserialize(jsonString) as PmCreateRequest

        assertThat(obj).isEqualTo(request)
    }
}