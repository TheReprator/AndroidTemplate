package app.compositeBuild.plugin

import app.compositeBuild.extra.*
import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import java.io.File
import java.io.FileInputStream
import java.util.Properties

class AppMainApplicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.configurePlugin {
            apply(AppPlugins.APP_ANDROID)

            if (File("${project.buildscript.sourceFile?.parent}/google-services.json").exists()) {
                apply(AppPlugins.APP_CRASHLYTICS)
                apply(AppPlugins.APP_GOOGLE_SERVICES)
            }
        }

        project.configureKapt()
        project.configureAndroid({

            minSdk = AndroidSdk.min
            targetSdk = AndroidSdk.target
            versionCode = AppVersion.versionCode
            versionName = AppVersion.versionName

            testInstrumentationRunner = AppInfo.testRunner
            buildConfigField("String", AppConstant.hostConstant, "\"${AppConstant.host}\"")

        }, {
            buildFeatures.apply {
                viewBinding = true
                buildConfig = true
            }

            lintOptions.apply {
                // Disable lintVital. Not needed since lint is run on CI
                isCheckReleaseBuilds = false
                // Ignore any tests
                isIgnoreTestSources = true
                // Make the build fail on any lint errors
                isAbortOnError = true
                // Allow lint to check dependencies
                isCheckDependencies = true
            }

            signingConfigs {
                getByName("debug") {
                    project.getKeyStoreConfig(this, "signing-debug.properties")
                }

                create("release") {
                    project.getKeyStoreConfig(this, "signing-release.properties")
                }
            }

            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                    applicationIdSuffix = ".debug"
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
                    proguardFiles.add(File("proguard-rules-chucker.pro"))
                }

                create("standard") {
                    dimension = "mode"
                }
            }
        })

        project.configureAndroidApplicationId()
    }


    private fun Project.configureAndroidApplicationId() {
        plugins.withId(AppPlugins.APP_ANDROID) {

            extensions.findByType<BaseAppModuleExtension>()?.run {
                defaultConfig {
                    applicationId = AppConstant.applicationPackage
                }
            }
        }
    }

    private fun Project.getKeyStoreConfig(
        defaultSigningConfig: ApkSigningConfig,
        propertyFileName: String
    ) {
        val properties = Properties()
        val propFile = File("${project.rootDir}/config/signingconfig/$propertyFileName")
        if (propFile.canRead() && propFile.exists()) {
            properties.load(FileInputStream(propFile))
            if (properties.containsKey("storeFile") && properties.containsKey("storePassword") &&
                properties.containsKey("keyAlias") && properties.containsKey("keyPassword")
            ) {
                defaultSigningConfig.storeFile =
                    File("${project.rootDir}/${properties.getProperty("storeFile")}")
                defaultSigningConfig.storePassword = properties.getProperty("storePassword")
                defaultSigningConfig.keyAlias = properties.getProperty("keyAlias")
                defaultSigningConfig.keyPassword = properties.getProperty("keyPassword")
                defaultSigningConfig.enableV2Signing = true
            }
        }
    }
}
