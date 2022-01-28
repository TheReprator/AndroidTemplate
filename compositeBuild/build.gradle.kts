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
