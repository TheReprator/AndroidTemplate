package app.compositeBuild.extra

object AndroidSdk {
    const val min = 21
    const val compile = 32
    const val target = compile

    val localesEnglish = "en"
    val localesHindi = "hi"
}

object AppConstant {
    const val applicationPackage = "app.root.androidtemplate"
    const val name = "AndroidTemplate"
    const val host = "https://example.com/"
    const val hostConstant = "HOST"
}

object AppVersion {
    const val versionCode = 1
    const val versionName = "1.0"
}


object AppInfo {
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}


object AppPlugins {
    const val APP_ANDROID = "com.android.application"
    const val APP_ANDROID_LIBRARY = "com.android.library"
    const val APP_KOTLIN_ANDROID = "kotlin-android"
    const val APP_KOTLIN_KAPT = "kotlin-kapt"
    const val APP_KOTLIN = "kotlin"
    const val APP_GOOGLE_SERVICES = "com.google.gms.google-services"
    const val APP_CRASHLYTICS = "com.google.firebase.crashlytics"


    const val APP_KAPT_ARGUMENTS = "dagger.hilt.shareTestComponents"
}