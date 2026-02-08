plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

group = "com.product.model"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}

tasks {
    val deploy: Task by creating {
        dependsOn("build")
    }
}
