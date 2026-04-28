plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {

    implementation(kotlin("stdlib"))
    api(libs.coroutines.core)
    api(libs.coroutines.test)
    implementation(project(":common"))
    implementation(project(":repo-common"))
    implementation(libs.kotest.junit5)
    implementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}