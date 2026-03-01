plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization) // чтобы сгенерился код с сериализаторами
    alias(libs.plugins.openapi.generator)
}

// Чтобы  сгенерированные файлы стали частью проекта, и компилятор Kotlin их увидел
sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

dependencies {
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.core)
}

/**
 * Настраиваем генерацию здесь
 */
openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1" // путь в build
    generatorName.set("kotlin") // Это и есть активный генератор
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set(rootProject.ext["spec-v1"] as String) // путь к файлу spec, берется из раздела ext корневого build.gradle.kts

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "kotlinx_serialization",
            "collectionType" to "list"
        )
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}