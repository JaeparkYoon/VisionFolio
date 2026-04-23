import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.model"
}
