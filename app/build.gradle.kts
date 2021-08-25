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

    buildTypes {

        getByName("debug") {
            isDebuggable = true
        }
    }

    buildFeatures.viewBinding = true
    buildFeatures.dataBinding = true
    buildFeatures.buildConfig = true

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

dependencies {
    implementation(project(AppModules.moduleBaseJava))
    implementation(project(AppModules.moduleBaseAndroid))

    implementation(project(AppModules.moduleNavigation))

    implementation(project(AppModules.moduleA))

    implementation(Libs.AndroidX.constraintlayout)

    kapt(Libs.AndroidX.Lifecycle.compiler)
    implementation(Libs.AndroidX.Lifecycle.extensions)

    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)

    implementation(Libs.AndroidX.multidex)

    implementation(Libs.OkHttp.loggingInterceptor)

    implementation(Libs.DaggerHilt.hilt)
    kapt(Libs.DaggerHilt.compiler)
}
