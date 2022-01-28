plugins {
    id("kotlin")
    id("version.gradle")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    sourceSets {
        map { it.java.srcDirs("src/${it.name}/kotlin") }
    }
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.kotlin.coroutines.core)

    api(libs.timber)

    api(libs.retrofit.retrofit)
    api(libs.retrofit.jacksonConverter)
    api(libs.jacksonKotlinModule)
}
