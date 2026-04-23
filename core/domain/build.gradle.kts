import com.android.build.api.dsl.LibraryExtension

plugins {
    id("visionfolio.android.library")
}

configure<LibraryExtension> {
    namespace = "jpyoon.example.visionfolio.core.domain"
}

dependencies {
    api(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common-kotlin"))
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
