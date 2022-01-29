package app.compositeBuild.plugin

import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.io.FileInputStream
import java.util.*

class AppMainApplicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.configurePlugins()
        project.configureAndroidCommonInfo()
        project.configureAndroidApplicationId()


        if (File("google-services.json").exists()) {
            project.pluginManager.apply {
                apply("com.google.firebase.crashlytics")
                apply("com.google.gms.google-services")
            }
        }
    }

    private fun Project.configurePlugins() {
        plugins.apply("com.android.application")
        plugins.apply("com.android.application")
        plugins.apply("kotlin-android")
        plugins.apply("kotlin-kapt")
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

            signingConfigs {
                getByName("debug") {
                    getKeyStoreConfig(this, "signing-debug.properties")
                }

                create("release")  {
                    getKeyStoreConfig(this, "signing-release.properties")
                }
            }

            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                }

                maybeCreate("beta").apply {
                    applicationIdSuffix = ".beta"
                    signingConfig = signingConfigs.getByName("debug")
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }

                maybeCreate("release").apply {
                    signingConfig = signingConfigs.getByName("release")
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            flavorDimensions("mode")

            productFlavors {
                create("qa") {
                    dimension = "mode"
                    applicationIdSuffix = ".qa"
                    proguardFiles.add(file("proguard-rules-chucker.pro"))
                }

                create("standard") {
                    dimension = "mode"
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

    private fun getKeyStoreConfig(defaultSigningConfig: ApkSigningConfig, propertyFileName: String) {
        val properties = Properties()
        val propFile = File("./config/signingconfig/$propertyFileName")
        if (propFile.canRead() && propFile.exists()) {
            properties.load(FileInputStream(propFile))
            if (properties.containsKey("storeFile") && properties.containsKey("storePassword") &&
                properties.containsKey("keyAlias") && properties.containsKey("keyPassword")
            ) {
                defaultSigningConfig.storeFile = File("../${properties.getProperty("storeFile")}")
                defaultSigningConfig.storePassword = properties.getProperty("storePassword")
                defaultSigningConfig.keyAlias = properties.getProperty("keyAlias")
                defaultSigningConfig.keyPassword = properties.getProperty("keyPassword")
                defaultSigningConfig.enableV2Signing = true
            }
        }
    }
}
