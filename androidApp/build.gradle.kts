import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("visionfolio.android.application")
    id("visionfolio.android.application.compose")
    id("visionfolio.kotlin.inject")
    alias(libs.plugins.kotlin.serialization)
}

configure<ApplicationExtension> {
    namespace = "jpyoon.example.visionfolio"

    defaultConfig {
        applicationId = "jpyoon.example.visionfolio"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common-android"))
    implementation(project(":core:common-kotlin"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:repository-api"))
    implementation(project(":core:local"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))
    implementation(project(":designsystem"))

    implementation(project(":feature:home"))
    implementation(project(":feature:trend"))
    implementation(project(":feature:upload"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:addholding"))
    implementation(project(":feature:tweaks"))
    implementation(project(":feature:dividend"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:returns"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.coil.compose)
    implementation(libs.okhttp)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite.bundled)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
