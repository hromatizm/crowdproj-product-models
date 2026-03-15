rootProject.name = "product-models"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}

include("api-v1")
include("common")
include("api-v1-mappers")
include("log")
include("app-ktor-rest")
include("app-kafka")