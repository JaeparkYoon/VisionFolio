import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension

plugins {
    id("visionfolio.kmp.library")
}

kotlin {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure(KotlinMultiplatformAndroidLibraryExtension::class.java) {
            namespace = "jpyoon.example.visionfolio.core.common"
        }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlin.inject.runtime)
        }
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
        }
    }
}
