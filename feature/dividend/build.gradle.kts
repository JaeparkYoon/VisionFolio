import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension

plugins {
    id("visionfolio.kmp.feature")
}

kotlin {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure(KotlinMultiplatformAndroidLibraryExtension::class.java) {
            namespace = "jpyoon.example.visionfolio.feature.dividend"
        }

    sourceSets {
    }
}
