package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppJVMPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("AppJVMPlugin")

        project.jvm()
    }

}