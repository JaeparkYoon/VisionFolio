import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
    id("visionfolio.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:local"))

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
