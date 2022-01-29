plugins {
    id("appMainApplicationPlugin")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
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

val installGitHook by tasks.registering(Copy::class) {
    from(File(rootProject.rootDir, "config/hooks/pre-push"))
    into(File(rootProject.rootDir, ".git/hooks/"))
    // https://github.com/gradle/kotlin-dsl-samples/issues/1412
    fileMode = 0b111101101 // -rwxr-xr-x
}

// tasks.getByPath(":app:preBuild").dependsOn(installGitHook)
