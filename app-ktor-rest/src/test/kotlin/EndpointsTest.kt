package com.example

import com.product.model.module
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.assertEquals

class EndpointsTest : FunSpec({

    test(("GET / returns 200 OK")) {
        testApplication {
            application { module() }
            client.get("/").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }
    }

    test("POST /v1/pm/create stub success returns expected product model") {
        testApplication {
            application { module() }

            val httpClient = createClient {
                install(ContentNegotiation) { json() }
            }

            val response = httpClient.post("/v1/pm/create") {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {
                      "requestType": "create",
                      "debug": {
                        "mode": "stub",
                        "stub": "success"
                      },
                      "pm": {
                        "name": "iPhone 15 Pro 256GB",
                        "description": "Тестовая модель товара для проверки create",
                        "productGroupId": "product-group-0001"
                      }
                    }
                    """.trimIndent()
                )
            }

            response.status shouldBe HttpStatusCode.OK

            val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
            body["result"]?.jsonPrimitive?.content shouldBe "success"

            val pm = body["pm"]!!.jsonObject
            pm["id"]?.jsonPrimitive?.content shouldBe "pm-1"
            pm["ownerId"]?.jsonPrimitive?.content shouldBe "user-1"
            pm["lock"]?.jsonPrimitive?.content shouldBe "lock-1"
            pm["name"]?.jsonPrimitive?.content shouldBe "Ноутбук ASUS ZenBook 14"
            pm["productGroupId"]?.jsonPrimitive?.content shouldBe "laptop-1"

            val permissions = pm["permissions"]!!.jsonArray.map { it.jsonPrimitive.content }
            permissions shouldBe listOf("read", "update", "delete")
        }
    }
})
