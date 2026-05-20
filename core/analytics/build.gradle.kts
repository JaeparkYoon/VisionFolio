import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
    id("visionfolio.android.library.compose")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.analytics"
}

dependencies {
    api(libs.timber)
    implementation(libs.androidx.core.ktx)
}
