plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.cache4k)
    implementation(libs.coroutines.core)
    implementation(libs.uuid)
    implementation(project(":common"))
    implementation(project(":repo-common"))
    testImplementation(libs.kotest.junit5)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}