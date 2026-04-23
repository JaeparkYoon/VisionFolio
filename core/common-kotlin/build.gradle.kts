import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.common"
}

dependencies {
    implementation(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
}
