plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":api-v1"))
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.core)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}