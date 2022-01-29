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
    implementation("com.android.tools.build:gradle:7.1.0")
    implementation(kotlin("gradle-plugin", "1.6.10"))
}