package version.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.jvm() {
    pluginManager.withPlugin("kotlin") {

        val javaPluginExtension =
            project.extensions.findByType(JavaPluginExtension::class.java) ?: throw Exception(
                "Not a java module. Did you forget to apply 'kotlin-jvm' plugin?"
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

fun Project.androidLibrary() {

    pluginManager.apply {
        apply("com.android.library")
        apply("kotlin-android")
        apply("kotlin-kapt")
    }

    val androidLibraryPluginExtension =
        project.extensions.findByType(LibraryExtension::class.java) ?: throw Exception(
            "Not an android library module. Did you forget to apply 'com.android.library' plugin?"
        )

    with(androidLibraryPluginExtension) {

        compileSdk = 32

        defaultConfig {
            minSdk = 21

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            consumerProguardFiles(
                file("proguard-rules.pro")
            )

            resourceConfigurations.add("en")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        with(sourceSets) {
            map { it.java.srcDirs("src/${it.name}/kotlin") }
        }

        testOptions {
            unitTests.apply {
                isReturnDefaultValues = true
                isIncludeAndroidResources = true
            }
        }

        buildFeatures.viewBinding = true

        packagingOptions {
            jniLibs.excludes.add("META-INF/*")
        }

        project.tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }
}

fun Project.appComponentLibrary() {

    pluginManager.apply {
        apply("com.android.library")
        apply("kotlin-android")
        apply("kotlin-kapt")
    }

    val androidLibraryPluginExtension =
        project.extensions.findByType(LibraryExtension::class.java) ?: throw Exception(
            "Not an android library module. Did you forget to apply 'com.android.library' plugin?"
        )


    with(androidLibraryPluginExtension) {

        compileSdk = 32

        defaultConfig {
            minSdk = 21

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            consumerProguardFiles(
                file("proguard-rules.pro")
            )

            resourceConfigurations.add("en")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        with(sourceSets) {
            map { it.java.srcDirs("src/${it.name}/kotlin") }
        }

        testOptions {
            unitTests.apply {
                isReturnDefaultValues = true
                isIncludeAndroidResources = true
            }
        }

        buildFeatures.viewBinding = true

        packagingOptions {
            jniLibs.excludes.add("META-INF/*")
        }

        project.tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }
}