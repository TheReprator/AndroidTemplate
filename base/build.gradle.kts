plugins {
    id("appJavaPlugin")
}

dependencies {
    api(kotlin("stdlib"))
    api(libs.kotlin.coroutines.core)

    api(libs.timber)

    api(libs.retrofit.retrofit)
    api(libs.retrofit.jacksonConverter)
    api(libs.jacksonKotlinModule)
}
