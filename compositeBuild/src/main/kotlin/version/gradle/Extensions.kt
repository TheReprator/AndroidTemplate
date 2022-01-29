package version.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.core.InternalBaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

fun Project.jvm() {
    project.pluginManager.withPlugin("kotlin") {

        val javaPluginExtension =
            project.extensions.findByType(JavaPluginExtension::class.java) ?: throw Exception(
                "Not an java module. Did you forget to apply 'kotlin(\"jvm\")' plugin?"
            )

        println("vikramJavak inside")

        with(javaPluginExtension) {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11

            with(sourceSets) {
                map { it.java.srcDirs("src/${it.name}/kotlin") }
            }
        }
    }
}

fun BaseExtension.variants(): DomainObjectSet<out InternalBaseVariant> {
    return when (this) {
        is AppExtension -> {
            applicationVariants
        }

        is LibraryExtension -> {
            libraryVariants
        }

        else -> throw GradleException("Unsupported BaseExtension type!")
    }
}