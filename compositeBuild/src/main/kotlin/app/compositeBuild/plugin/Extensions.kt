package app.compositeBuild.plugin

import app.compositeBuild.extra.AndroidSdk
import app.compositeBuild.extra.AppInfo
import app.compositeBuild.extra.AppPlugins
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginManager
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.jvm() {

    project.pluginManager.apply {
        apply(AppPlugins.APP_KOTLIN)
    }

    val javaPluginExtension = project.extensions.findByType(JavaPluginExtension::class.java)!!
    with(javaPluginExtension) {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        with(sourceSets) {
            map { it.java.srcDirs("src/${it.name}/kotlin") }
        }
    }
}


fun Project.configurePlugin(pluginManagerBlock: PluginManager.() -> Unit = {}) {
    pluginManager.apply {
        apply(AppPlugins.APP_KOTLIN_ANDROID)
        apply(AppPlugins.APP_KOTLIN_KAPT)

        pluginManagerBlock(this)
    }
}

fun Project.configureKapt() {
    project.pluginManager.withPlugin(AppPlugins.APP_KOTLIN_KAPT) {

        val kotlinKaptPluginExtension =
            project.extensions.findByType(KaptExtension::class.java)
                ?: throw Exception("Not an kapt module. Did you forget to apply 'kotlin-kapt' plugin?")

        with(kotlinKaptPluginExtension) {
            correctErrorTypes = true
            useBuildCache = true

            arguments {
                arg(AppPlugins.APP_KAPT_ARGUMENTS, "true")
            }
        }
    }
}

fun Project.configureAndroid(
    defaultConfigBlock: DefaultConfig.() -> Unit,
    baseExtensionBlock: BaseExtension.() -> Unit = {}
) {

    val androidLibraryPluginExtension =
        project.extensions.findByType(BaseExtension::class.java)!!

    with(androidLibraryPluginExtension) {

        compileSdkVersion(AndroidSdk.compile)

        defaultConfig {
            minSdk = AndroidSdk.min

            resourceConfigurations.add(AndroidSdk.localesEnglish)
            defaultConfigBlock(this)
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

        baseExtensionBlock(this)
    }
}

fun Project.androidLibrary() {
    configurePlugin {
        apply(AppPlugins.APP_ANDROID_LIBRARY)
    }

    configureKapt()

    configureAndroid({
        testInstrumentationRunner = AppInfo.testRunner
    })
}

fun Project.appComponentLibrary() {

    configurePlugin {
        apply(AppPlugins.APP_ANDROID_LIBRARY)
    }

    configureKapt()

    configureAndroid({
        testInstrumentationRunner = AppInfo.testRunner
    })
}