import com.android.build.api.dsl.ApkSigningConfig
import java.io.FileInputStream
import java.util.Properties

plugins {
    id(Libs.Plugins.androidApplication)
    kotlin(Libs.Plugins.kotlinAndroid)
    kotlin(Libs.Plugins.kotlinKapt)
    id(Libs.Plugins.kotlinNavigation)
    id(Libs.Plugins.kaptDagger)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true

    arguments {
        arg("dagger.hilt.shareTestComponents", "true")
    }
}

fun getKeyStoreConfig(defaultSigningConfig: ApkSigningConfig, propertyFileName: String) {
    val properties = Properties()
    val propFile = File("./config/signingconfig/$propertyFileName")
    if (propFile.canRead() && propFile.exists()) {
        properties.load(FileInputStream(propFile))
        if (properties.containsKey("storeFile") && properties.containsKey("storePassword") &&
            properties.containsKey("keyAlias") && properties.containsKey("keyPassword")
        ) {
            defaultSigningConfig.storeFile = file("../${properties.getProperty("storeFile")}")
            defaultSigningConfig.storePassword = properties.getProperty("storePassword")
            defaultSigningConfig.keyAlias = properties.getProperty("keyAlias")
            defaultSigningConfig.keyPassword = properties.getProperty("keyPassword")
            defaultSigningConfig.enableV2Signing = true
        }
    }
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        applicationId = AppConstant.applicationPackage

        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target

        versionCode = AppVersion.versionCode
        versionName = AppVersion.versionName

        testInstrumentationRunner = Libs.TestDependencies.testRunner

        multiDexEnabled = true

        resourceConfigurations.add(AndroidSdk.localesEnglish)

        buildConfigField("String", AppConstant.hostConstant, "\"${AppConstant.host}\"")
    }

    signingConfigs {
        getByName("debug") {
            getKeyStoreConfig(this, "signing-debug.properties")
        }
    }

    buildTypes {

        getByName("debug") {
            isDebuggable = true
        }
    }

    flavorDimensions.add("mode")

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

    buildFeatures {
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

val qaImplementation by configurations

dependencies {
    implementation(project(AppModules.moduleBaseJava))
    implementation(project(AppModules.moduleBaseAndroid))
    implementation(project(AppModules.moduleWork))
    implementation(project(AppModules.moduleLocation))
    implementation(project(AppModules.moduleSetting))

    implementation(project(AppModules.moduleNavigation))

    implementation(project(AppModules.moduleA))

    implementation(Libs.AndroidX.constraintlayout)

    kapt(Libs.AndroidX.Lifecycle.compiler)
    implementation(Libs.AndroidX.Lifecycle.extensions)

    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)

    implementation(Libs.AndroidX.multidex)

    implementation(Libs.AndroidX.preference)

    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.crashlytics)

    implementation(Libs.DaggerHilt.hilt)
    kapt(Libs.DaggerHilt.compiler)

    implementation(Libs.DaggerHilt.work)
    kapt(Libs.DaggerHilt.hiltCompiler)

    qaImplementation(Libs.chucker.chucker)

    qaImplementation(Libs.chucker.debugDrawer)
    qaImplementation(Libs.chucker.leakcanary)
    qaImplementation(Libs.chucker.retrofit)
    qaImplementation(Libs.chucker.timber)
    qaImplementation(Libs.chucker.okhttplogger)

    qaImplementation(Libs.Retrofit.mock)

    qaImplementation(Libs.OkHttp.loggingInterceptor)
    qaImplementation(Libs.leakCanary)
}

if (file("google-services.json").exists()) {
    plugins {
        id(Libs.Plugins.crashlytics)
        id(Libs.Plugins.googleServices)
    }
}


val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "config/hooks/pre-push"))
    into(File(rootProject.rootDir, ".git/hooks/"))
    // https://github.com/gradle/kotlin-dsl-samples/issues/1412
    fileMode = 0b111101101 // -rwxr-xr-x
}

tasks.getByPath(":app:preBuild").dependsOn(installGitHook)
