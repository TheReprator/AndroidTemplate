object AndroidSdk {
    const val min = 21
    const val compile = 32
    const val target = compile

    val localesEnglish = "en"
    val localesHindi = "hi"
}

object AppConstant {
    const val applicationPackage = "app.root.androidtemplate"
    const val name = "AndroidTemplate"
    const val host = "https://example.com/"
    const val hostConstant = "HOST"
}

object AppVersion {
    const val versionCode = 1
    const val versionName = "1.0"
}

object AppModules {
    const val moduleApp = ":app"
    const val moduleBaseAndroid = ":base-android"
    const val moduleBaseJava = ":base"
    const val moduleNavigation = ":navigation"
    const val moduleWork = ":workTask"

    const val moduleA = ":appModules:moduleA"
    const val moduleLocation = ":appModules:location"
    const val moduleSetting = ":appModules:settings"
}

object Libs {

    object chucker {
        private const val version = "3.5.2"
        const val chucker = "com.github.chuckerteam.chucker:library:$version"

        private const val debugDrawerVersion = "0.9.8-SNAPSHOT"
        const val debugDrawer = "au.com.gridstone.debugdrawer:debugdrawer:$debugDrawerVersion"
        const val leakcanary = "au.com.gridstone.debugdrawer:debugdrawer-leakcanary:$debugDrawerVersion"
        const val retrofit = "au.com.gridstone.debugdrawer:debugdrawer-retrofit:$debugDrawerVersion"
        const val timber = "au.com.gridstone.debugdrawer:debugdrawer-timber:$debugDrawerVersion"
        const val okhttplogger = "au.com.gridstone.debugdrawer:debugdrawer-okhttp-logger:$debugDrawerVersion"
    }

    object Versions{
        const val kotlin = "1.6.10"
        const val spotless = "6.1.0"
        const val dokka = "1.6.10"
        const val ktlint = "0.43.2"
        const val detekt = "1.19.0"
        const val dependencyUpdate = "0.40.0"
    }

    object Plugins {
        const val androidApplication = "com.android.application"
        const val crashlytics = "com.google.firebase.crashlytics"
        const val googleServices = "com.google.gms.google-services"
        const val androidLibrary = "com.android.library"
        const val kotlinLibrary = "kotlin"
        const val kotlinJVM = "jvm"
        const val kotlinAndroid = "android"
        const val kotlinKapt = "kapt"
        const val kaptDagger = "dagger.hilt.android.plugin"
        const val kotlinNavigation = "androidx.navigation.safeargs.kotlin"
        const val dokka = "org.jetbrains.dokka"
        const val spotless = "com.diffplug.spotless"
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val dependencyUpdateVersions = "com.github.ben-manes.versions"
    }

    const val inject = "javax.inject:javax.inject:1"
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:2.8.1"
    const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"


    object Google {
        const val materialWidget = "com.google.android.material:material:1.4.0"
        const val gmsGoogleServices = "com.google.gms:google-services:4.3.10"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:${Versions.kotlin}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val cardView = "androidx.cardview:cardview:1.0.0"
        const val annotation = "androidx.annotation:annotation:1.3.0"
        const val multidex = "androidx.multidex:multidex:2.0.1"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.2"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"
        const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"

        const val browser = "androidx.browser:browser:1.3.0"
        const val preferenceKtx = "androidx.preference:preference-ktx:1.2.0-rc01"

        object Navigation {
            private const val version = "2.3.5"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val navigationPlugin =
                "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            const val test = "androidx.navigation:navigation-testing:$version"
        }
    }

    object OkHttp {
        private const val version = "4.9.3"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object DaggerHilt {
        private const val version = "2.40.5"
        private const val androidXversion = "1.0.0"

        const val classPath = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val test = "com.google.dagger:hilt-android-testing:$version"

        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"

        const val work = "androidx.hilt:hilt-work:$androidXversion"
        const val hiltCompiler = "androidx.hilt:hilt-compiler:$androidXversion"
    }

    object TestDependencies {

        const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val core = "androidx.arch.core:core-testing:2.1.0"

        object Junit5 {
            const val classPath = "de.mannodermaus.gradle.plugins:android-junit5:1.8.2.0"
            const val plugin = "de.mannodermaus.android-junit5"

            private const val version = "5.8.2"

            // (Required) Writing and executing Unit Tests on the JUnit Platform
            const val api = "org.junit.jupiter:junit-jupiter-api:$version"
            const val runtime = "org.junit.jupiter:junit-jupiter-engine:$version"

            const val platformSuite = "org.junit.platform:junit-platform-suite:1.8.2"

            // (Optional) If you need "Parameterized Tests"
            const val parameterized = "org.junit.jupiter:junit-jupiter-params:$version"

            // (Optional) If you also have JUnit 4-based tests
            const val junit4Runtime = "org.junit.vintage:junit-vintage-engine:$version"
        }
    }
}