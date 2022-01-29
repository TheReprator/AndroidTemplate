plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

gradlePlugin {
    plugins.register("version.gradle") {
            id = "version.gradle"
            implementationClass = "version.gradle.VersionGradle"
    }
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:7.1.0")
    implementation(kotlin("gradle-plugin", "1.6.10"))
    implementation(kotlin("android-extensions"))
}