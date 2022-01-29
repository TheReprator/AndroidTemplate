package app.compositeBuild.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppComponentPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("AppComponentPlugin")
        project.appComponentLibrary()
    }

}
