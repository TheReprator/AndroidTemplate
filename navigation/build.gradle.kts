plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("version.gradle")
    id("androidx.navigation.safeargs.kotlin")
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min

        consumerProguardFiles(
            file("proguard-rules.pro")
        )
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
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }

    packagingOptions {
        jniLibs.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(project(AppModules.moduleBaseAndroid))

    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
}
