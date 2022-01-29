plugins {
    id("appComponentPlugin")
}

android {

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                file("proguard-rules.pro")
            )
        }
    }

}

dependencies {
    implementation(project(AppModules.moduleBaseJava))
    implementation(project(AppModules.moduleBaseAndroid))

    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

    implementation(libs.androidx.workmanager.work)
    androidTestImplementation(libs.androidx.workmanager.testing)
}
