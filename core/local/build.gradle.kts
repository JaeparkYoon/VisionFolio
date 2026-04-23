import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
    id("visionfolio.android.room")
    id("visionfolio.android.hilt")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.local"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.kotlinx.serialization.json)
}
