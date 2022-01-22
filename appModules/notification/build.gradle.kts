plugins {
    id(Libs.Plugins.androidLibrary)
    kotlin(Libs.Plugins.kotlinAndroid)
    kotlin(Libs.Plugins.kotlinKapt)
    id(Libs.Plugins.kotlinNavigation)
    id(Libs.Plugins.kaptDagger)
    id(Libs.TestDependencies.Junit5.plugin)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target

        multiDexEnabled = true

        consumerProguardFiles(
            file("proguard-rules.pro")
        )

        resourceConfigurations.add(AndroidSdk.localesEnglish)
    }

    buildFeatures.viewBinding = true

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        map { it.java.srcDirs("src/${it.name}/kotlin") }
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                file("proguard-rules.pro")
            )
        }
    }

    packagingOptions {
        jniLibs.excludes.add("META-INF/atomicfu.kotlin_module")
        jniLibs.excludes.add("META-INF/*")
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
        animationsDisabled = true
    }

    sourceSets {
        getByName("test").java.srcDirs("src/test/kotlin/", "src/sharedTest/kotlin/")
        getByName("test").resources.srcDirs("src/sharedTest/resources/")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin/", "src/sharedTest/kotlin/")
        getByName("androidTest").resources.srcDirs("src/sharedTest/resources/")
    }
}

dependencies {
    implementation(project(AppModules.moduleBaseJava))
    implementation(project(AppModules.moduleBaseAndroid))
    implementation(project(AppModules.moduleNavigation))
    implementation(project(AppModules.moduleNotification))

    implementation(Libs.AndroidX.Navigation.fragmentKtx)

    implementation(Libs.DaggerHilt.hilt)
    kapt(Libs.DaggerHilt.compiler)

}
