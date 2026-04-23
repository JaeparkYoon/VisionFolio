import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.feature")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.feature.tweaks"
}
