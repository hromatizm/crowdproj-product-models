plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)
    implementation(libs.logback.classic)

    implementation(project(":common"))
    implementation(project(":api-v1"))
    implementation(project(":api-v1-mappers"))
    implementation(project(":log"))

    testImplementation(libs.kotest.junit5)
    testImplementation(kotlin("test"))
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.product.model.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("all")
}

tasks.test {
    useJUnitPlatform()
}
