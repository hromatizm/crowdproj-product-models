plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":api-v1"))
    testImplementation(kotlin("test"))
    testImplementation(libs.assertj)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}