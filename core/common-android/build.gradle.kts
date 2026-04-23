import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.android"
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
