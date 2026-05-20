import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.android"
}

dependencies {
    api(project(":core:common-kotlin"))
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
