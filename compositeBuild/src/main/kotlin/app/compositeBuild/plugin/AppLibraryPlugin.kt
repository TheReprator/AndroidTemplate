package app.compositeBuild.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.androidLibrary()
    }
}