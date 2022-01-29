package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class VersionGradle : Plugin<Project> {
    override fun apply(project: Project) {

        /*  project.pluginManager.withPlugin("com.android.application") {
              println("vikramAndroidApplication")
          }

          project.pluginManager.withPlugin("com.android.library") {
              println("vikramAndroidLibrary")
          }

          project.pluginManager.withPlugin("kotlin") {
              println("vikramKotlin")
          }*/

        project.pluginManager.withPlugin("kotlin-kapt") {

            val kotlinKaptPluginExtension =
                project.extensions.findByType(KaptExtension::class.java) ?:
                    throw Exception("Not an java module. Did you forget to apply 'kotlin-kapt' plugin?")

            with(kotlinKaptPluginExtension) {
                correctErrorTypes = true
                useBuildCache = true

                arguments {
                    arg("dagger.hilt.shareTestComponents", "true")
                }
            }
        }

        if (project.pluginManager.hasPlugin("kotlin")) {
            project.jvm()
        } else if (project.pluginManager.hasPlugin("com.android.library")) {
            project.androidLibrary()
        } else if (project.pluginManager.hasPlugin("com.android.application")) {
            println("vikramAndroidApplication")
        }
    }
}