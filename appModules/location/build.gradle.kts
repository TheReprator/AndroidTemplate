plugins {
    id("appComponentPlugin")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(AppModules.moduleBaseJava))
    implementation(project(AppModules.moduleBaseAndroid))
    implementation(project(AppModules.moduleNavigation))

    implementation(libs.google.location)

    implementation(libs.androidx.navigation.fragment)

    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)
}
