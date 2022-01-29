package app.compositeBuild.plugin

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AppMainApplicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("AppMainApplicationPlugin")
        project.configurePlugins()
        project.configureAndroidCommonInfo()
        project.configureAndroidApplicationId()
    }

    private fun Project.configurePlugins() {
        plugins.apply("com.android.application")
    }

    private fun Project.configureAndroidCommonInfo() {
        extensions.findByType<BaseExtension>()?.run {
            compileSdkVersion(32)

            defaultConfig {
                minSdk = 21
                targetSdk = 32
                versionCode = 1
                versionName = "1.0"

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                resourceConfigurations.add("en")

                buildConfigField("String", "HOST", " \"https://example.com/\"")
            }

            buildFeatures.apply {
                viewBinding = true
                buildConfig = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            sourceSets {
                map { it.java.srcDirs("src/${it.name}/kotlin") }
            }

            testOptions {
                unitTests.isReturnDefaultValues = true
                unitTests.isIncludeAndroidResources = true
            }

            project.tasks.withType(KotlinCompile::class.java).configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_11.toString()
                }
            }

            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                }

                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }

        }
    }

    private fun Project.configureAndroidApplicationId() {
        plugins.withId("com.android.application") {

            extensions.findByType<BaseAppModuleExtension>()?.run {
                defaultConfig {
                    applicationId = "app.root.androidtemplate"
                }
            }
        }
    }
}
