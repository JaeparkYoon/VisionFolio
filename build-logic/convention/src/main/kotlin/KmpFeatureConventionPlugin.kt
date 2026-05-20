import jpyoon.example.visionfolio.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("visionfolio.kmp.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                targetKotlinPlatforms.set(setOf(KotlinPlatformType.androidJvm))
            }

            dependencies {
                // commonMain
                add("commonMainImplementation", project(":core:common-kotlin"))
                add("commonMainImplementation", project(":core:model"))
                add("commonMainImplementation", project(":core:domain"))
                add("commonMainImplementation", project(":core:repository-api"))
                add("commonMainImplementation", libs.findLibrary("kotlin-inject-runtime").get())

                // androidMain — project deps
                add("androidMainImplementation", project(":core:model-resources"))
                add("androidMainImplementation", project(":core:navigation"))
                add("androidMainImplementation", project(":core:analytics"))
                add("androidMainImplementation", project(":designsystem"))

                // androidMain — Compose BOM
                add("androidMainImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("androidMainImplementation", libs.findLibrary("androidx-compose-ui").get())
                add("androidMainImplementation", libs.findLibrary("androidx-compose-ui-graphics").get())
                add("androidMainImplementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                add("androidMainImplementation", libs.findLibrary("androidx-compose-material3").get())
                add("androidMainImplementation", libs.findLibrary("androidx-compose-material-icons-extended").get())

                // androidMain — Lifecycle / Navigation
                add("androidMainImplementation", libs.findLibrary("androidx-core-ktx").get())
                add("androidMainImplementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("androidMainImplementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                add("androidMainImplementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                add("androidMainImplementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                add("androidMainImplementation", libs.findLibrary("androidx-navigation-compose").get())

                // KSP — kotlin-inject
                add("kspAndroid", libs.findLibrary("kotlin-inject-compiler").get())
                add("kspIosArm64", libs.findLibrary("kotlin-inject-compiler").get())
                add("kspIosX64", libs.findLibrary("kotlin-inject-compiler").get())
                add("kspIosSimulatorArm64", libs.findLibrary("kotlin-inject-compiler").get())
            }
        }
    }
}
