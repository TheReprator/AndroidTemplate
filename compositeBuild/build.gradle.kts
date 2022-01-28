plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins.register("version.gradle") {
            id = "version.gradle"
            implementationClass = "version.gradle.VersionGradle"
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("com.squareup:javapoet:1.13.0")
    implementation(gradleApi()) // for custom plugins
}

