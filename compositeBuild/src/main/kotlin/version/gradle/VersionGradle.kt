package version.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

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

        if(project.pluginManager.hasPlugin("kotlin")){
            println("vikramJava")
            project.jvm()
        }else if(project.pluginManager.hasPlugin("com.android.library")) {
            println("vikramAndroidLibrary")
        }else if(project.pluginManager.hasPlugin("com.android.application")){
            println("vikramAndroidApplication")
        }
    }
}