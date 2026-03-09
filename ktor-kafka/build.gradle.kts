plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.kafka) {
        exclude(group = "io.confluent")
    }
    implementation(libs.ktor.server.call.logging)
    implementation(libs.logback.classic)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.core)

    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":api-v1-mappers"))
    implementation(project(":log"))

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotest.junit5)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
