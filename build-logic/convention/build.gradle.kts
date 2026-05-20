plugins {
    `kotlin-dsl`
}

group = "jpyoon.example.visionfolio.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.androidx.room.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "visionfolio.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "visionfolio.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "visionfolio.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "visionfolio.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidRoom") {
            id = "visionfolio.android.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("kmpLibrary") {
            id = "visionfolio.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpFeature") {
            id = "visionfolio.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("kotlinInject") {
            id = "visionfolio.kotlin.inject"
            implementationClass = "KotlinInjectConventionPlugin"
        }
    }
}
