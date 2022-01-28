plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("version.gradle")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
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
        testInstrumentationRunner = "app.module.modulea.util.ModuleATestRunner"
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

configurations.all {
    resolutionStrategy {
        eachDependency {
            when (requested.module.toString()) {
                "com.squareup.okhttp3:okhttp" -> useVersion("4.9.1")
            }
        }
    }
}

dependencies {
    implementation(project(AppModules.moduleBaseJava))
    implementation(project(AppModules.moduleBaseAndroid))
    implementation(project(AppModules.moduleNavigation))

    implementation(libs.androidx.widget.cardView)
    implementation(libs.androidx.widget.swiperefresh)

    implementation(libs.androidx.navigation.fragment)

    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)

    /*
    *  Unit Testing
    * */
    testImplementation(libs.junit5.platformSuite)
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.runtime)

    testImplementation(libs.test.archCoreTesting)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.test.mockK)

    testImplementation(libs.kotlin.coroutines.test)

    /*
       UI Tests
    */
    implementation(libs.uiTest.busyBee)

    debugImplementation(libs.uiTest.fragmentTesting)
    androidTestImplementation(libs.uiTest.fragmentRuntime)

    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.compiler)

    androidTestImplementation(libs.androidx.test.junit)

    androidTestImplementation(libs.uiTest.kaspresso)

    androidTestImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.okhttp.loggingInterceptor)

    // OkHttp Idling Resource
    androidTestImplementation(libs.uiTest.okhttpIdlingResource)

    androidTestImplementation(libs.kotlin.coroutines.test)
}
