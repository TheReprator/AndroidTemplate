plugins {
    id(Libs.Plugins.androidLibrary)
    kotlin(Libs.Plugins.kotlinAndroid)
    kotlin(Libs.Plugins.kotlinKapt)
}

android {

    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min

        testInstrumentationRunner = Libs.TestDependencies.testRunner

        consumerProguardFiles(
            file("proguard-rules.pro")
        )

        resourceConfigurations.add(AndroidSdk.localesEnglish)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    sourceSets {
        map { it.java.srcDirs("src/${it.name}/kotlin") }
    }

    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    buildFeatures.dataBinding = true

    packagingOptions {
        jniLibs.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)

    api(Libs.AndroidX.Fragment.fragment)

    api(Libs.AndroidX.recyclerview)

    api(Libs.Google.materialWidget)
    api(Libs.AndroidX.coreKtx)
    api(Libs.AndroidX.appcompat)

    api(Libs.Coil.coil)
}
