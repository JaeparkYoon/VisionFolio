import jpyoon.example.visionfolio.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Feature modules share common dependencies: common-android (MVI), model, domain,
 * designsystem, navigation, plus lifecycle / navigation-compose / hilt-navigation-compose.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("visionfolio.android.library")
            pluginManager.apply("visionfolio.android.library.compose")
            pluginManager.apply("visionfolio.android.hilt")

            dependencies {
                add("implementation", project(":core:common-android"))
                add("implementation", project(":core:common-kotlin"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:navigation"))
                add("implementation", project(":core:analytics"))
                add("implementation", project(":designsystem"))

                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
                add("implementation", libs.findLibrary("hilt-navigation-compose").get())
            }
        }
    }
}
