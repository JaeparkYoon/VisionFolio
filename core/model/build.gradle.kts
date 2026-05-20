import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension

plugins {
    id("visionfolio.kmp.library")
}

kotlin {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure(KotlinMultiplatformAndroidLibraryExtension::class.java) {
            namespace = "jpyoon.example.visionfolio.core.model"
        }
}
