import jpyoon.example.visionfolio.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinInjectConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                add("implementation", libs.findLibrary("kotlin-inject-runtime").get())
                add("ksp", libs.findLibrary("kotlin-inject-compiler").get())
            }
        }
    }
}
