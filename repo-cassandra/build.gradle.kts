plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.jdk9)
    implementation(libs.uuid)
    implementation(libs.bundles.cassandra)
    kapt(libs.db.cassandra.kapt)

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