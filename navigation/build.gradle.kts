plugins {
    id("appComponentPlugin")
    id("androidx.navigation.safeargs.kotlin")
}

dependencies {
    implementation(project(AppModules.moduleBaseAndroid))

    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
}
