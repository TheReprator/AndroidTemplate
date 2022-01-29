package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AppLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("AppLibraryPlugin")
        project.androidLibrary()
    }
}