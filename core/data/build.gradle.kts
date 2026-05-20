import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension

plugins {
    id("visionfolio.kmp.library")
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure(KotlinMultiplatformAndroidLibraryExtension::class.java) {
            namespace = "jpyoon.example.visionfolio.core.data"
        }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:repository-api"))
            implementation(project(":core:common-kotlin"))
            implementation(project(":core:model"))
            implementation(project(":core:network"))
            implementation(project(":core:local"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlin.inject.runtime)
        }
        androidMain.dependencies {
            implementation(project(":core:analytics"))
            implementation(project(":core:common-android"))
            implementation(project(":core:model-resources"))
            implementation(libs.androidx.datastore.preferences)
        }
    }
}

dependencies {
    add("kspAndroid", libs.kotlin.inject.compiler)
    add("kspIosArm64", libs.kotlin.inject.compiler)
    add("kspIosX64", libs.kotlin.inject.compiler)
    add("kspIosSimulatorArm64", libs.kotlin.inject.compiler)
}
