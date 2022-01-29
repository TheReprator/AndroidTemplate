plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("version.gradle")
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
