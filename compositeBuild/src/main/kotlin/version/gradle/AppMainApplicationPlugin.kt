package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppMainApplicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("AppMainApplicationPlugin")
    }

}
