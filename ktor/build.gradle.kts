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
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.headers.caching)
    implementation(libs.ktor.server.headers.default)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.logback.classic)

    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":api-v1-mappers"))
    implementation(project(":log"))
    implementation("io.ktor:ktor-serialization-jackson:3.4.0")

    testImplementation(libs.kotest.junit5)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.negotiation)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}