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
    implementation(project(":repo-test"))

    testImplementation(libs.kotest.junit5)
    testImplementation(kotlin("test"))

}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}