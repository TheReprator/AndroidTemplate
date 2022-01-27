plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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

    buildFeatures.viewBinding = true

    packagingOptions {
        jniLibs.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    api(libs.androidx.core)
    api(libs.androidx.appCompat)
    api(libs.coil.coil)
    api(libs.google.material)
    api(libs.androidx.fragment)
    api(libs.androidx.widget.reyclerview)
    api(libs.androidx.widget.constraintlayout)
}
