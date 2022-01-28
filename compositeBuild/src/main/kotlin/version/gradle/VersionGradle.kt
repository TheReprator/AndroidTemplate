import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionGradle : Plugin<Project> {
    override fun apply(project: Project) {

        if(project.hasProperty("kotlin")){
            println("vikramJava")
        }else if(project.hasProperty("com.android.library")) {
            println("vikramAndroidLibrary")
        }else if(project.hasProperty("com.android.application")){
            println("vikramAndroidApplication")
        }

    }
}