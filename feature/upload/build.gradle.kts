import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.feature")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.feature.upload"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
}
