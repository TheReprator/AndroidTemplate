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

object AppModules {
    const val moduleApp = ":app"
    const val moduleBaseAndroid = ":base-android"
    const val moduleBaseJava = ":base"
    const val moduleNavigation = ":navigation"
    const val moduleWork = ":workTask"

    const val moduleA = ":appModules:moduleA"
    const val moduleLocation = ":appModules:location"
    const val moduleSetting = ":appModules:settings"
}