import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension

plugins {
    id("visionfolio.kmp.library")
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    (this as org.gradle.api.plugins.ExtensionAware).extensions
        .configure(KotlinMultiplatformAndroidLibraryExtension::class.java) {
            namespace = "jpyoon.example.visionfolio.core.navigation"
        }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.material.icons.extended)
            implementation(libs.androidx.compose.material3)
        }
    }
}

dependencies {
    add("androidMainImplementation", platform(libs.androidx.compose.bom))
}
