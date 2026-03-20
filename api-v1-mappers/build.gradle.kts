plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":log"))
    testImplementation(libs.kotest.junit5)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}