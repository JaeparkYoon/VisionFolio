import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
    id("visionfolio.android.library.compose")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.model.resources"
}

dependencies {
    implementation(project(":core:model"))
}
