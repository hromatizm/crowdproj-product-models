plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":libs"))
    implementation(project(":log"))
    implementation(project(":common"))

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.coroutines.test)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}