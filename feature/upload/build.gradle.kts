import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension

plugins {
    id("visionfolio.kmp.feature")
}

kotlin {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure(KotlinMultiplatformAndroidLibraryExtension::class.java) {
            namespace = "jpyoon.example.visionfolio.feature.upload"
        }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.coil.compose)
        }
    }
}
