package app.compositeBuild.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppJVMPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.jvm()
    }

}