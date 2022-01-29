package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.LoggerFactory

class AppComponentPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("AppComponentPlugin")
        project.appComponentLibrary()
    }

}
