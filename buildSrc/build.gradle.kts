plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("version.gradle") {
            id = "version.gradle"
            implementationClass = "app.template.buildsrc.VersionGradle"
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("com.squareup:javapoet:1.13.0")
}
