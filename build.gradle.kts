plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

group = "com.product.model"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("./specs")
    set("spec-v1", specDir.file("spec-v1.yaml").toString())
}

tasks {
    register("build" ) {
        group = "build"
    }
    register("check" ) {
        group = "verification"
        // Таска верхнего уровня запустит таски check всех подпроектов
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@register.dependsOn(it)
            }
        }
    }
}