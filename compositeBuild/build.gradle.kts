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
    plugins.register("appJavaPlugin") {
            id = "appJavaPlugin"
            implementationClass = "version.gradle.AppJVMPlugin"
    }

    plugins.register("appLibraryPlugin") {
            id = "appLibraryPlugin"
            implementationClass = "version.gradle.AppLibraryPlugin"
    }

    plugins.register("appComponentPlugin") {
            id = "appComponentPlugin"
            implementationClass = "version.gradle.AppComponentPlugin"
    }

    plugins.register("appMainApplicationPlugin") {
            id = "appMainApplicationPlugin"
            implementationClass = "version.gradle.AppMainApplicationPlugin"
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.0")
    implementation(kotlin("gradle-plugin", "1.6.10"))
}