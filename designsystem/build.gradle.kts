import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
    id("visionfolio.android.library.compose")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
}
