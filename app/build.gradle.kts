import com.android.build.api.dsl.ApkSigningConfig
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("appMainApplicationPlugin")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

    implementation(libs.google.analytics)
    implementation(libs.google.crashlytics)

    implementation(libs.androidx.lifecycle.process)

    qaImplementation(libs.chucker.library)

    qaImplementation(libs.debugdrawer.debugdrawer)
    qaImplementation(libs.debugdrawer.leakcanary)
    qaImplementation(libs.debugdrawer.retrofit)
    qaImplementation(libs.retrofit.mock)
    qaImplementation(libs.debugdrawer.timber)
    qaImplementation(libs.debugdrawer.okhttplogger)

    qaImplementation(libs.leakCanary)
    qaImplementation(libs.okhttp.loggingInterceptor)
}

if (file("google-services.json").exists()) {
    plugins {
        id("com.google.firebase.crashlytics")
        id("com.google.gms.google-services")
    }
}

val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "config/hooks/pre-push"))
    into(File(rootProject.rootDir, ".git/hooks/"))
    // https://github.com/gradle/kotlin-dsl-samples/issues/1412
    fileMode = 0b111101101 // -rwxr-xr-x
}

// tasks.getByPath(":app:preBuild").dependsOn(installGitHook)
