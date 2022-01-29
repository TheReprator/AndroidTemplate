package version.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

class AppJVMPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("AppJVMPlugin")

        project.pluginManager.apply {
            apply("kotlin")
        }

        val javaPluginExtension =
            project.extensions.findByType(JavaPluginExtension::class.java) ?: throw Exception(
                "Not an Android application. Did you forget to apply 'com.android.application' plugin?"
            )

        with(javaPluginExtension) {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11

            with(sourceSets) {
                map { it.java.srcDirs("src/${it.name}/kotlin") }
            }
        }

    }

}