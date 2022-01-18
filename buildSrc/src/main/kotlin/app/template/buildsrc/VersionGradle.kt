package app.template.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project


class VersionGradle : Plugin<Project> {
    override fun apply(project: Project) {
    }
}


object ProjectVersion {
    const val kotlin = "1.6.10"
    const val spotless = "6.1.0"
    const val dokka = "1.6.10"
    const val ktlint = "0.43.2"
    const val detekt = "1.19.0"
    const val dependencyUpdate = "0.40.0"
}