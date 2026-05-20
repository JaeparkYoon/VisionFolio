pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VisionFolio"

include(":androidApp")
include(":shared")

include(":core:common-android")
include(":core:common-kotlin")
include(":core:model")
include(":core:model-resources")
include(":core:domain")
include(":core:data")
include(":core:repository-api")
include(":core:network")
include(":core:local")
include(":core:navigation")
include(":core:analytics")

include(":designsystem")

include(":feature:home")
include(":feature:trend")
include(":feature:dividend")
include(":feature:upload")
include(":feature:settings")
include(":feature:addholding")
include(":feature:tweaks")
include(":feature:chat")
include(":feature:returns")
