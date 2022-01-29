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
            implementationClass = "app.compositeBuild.plugin.AppJVMPlugin"
    }

    plugins.register("appLibraryPlugin") {
            id = "appLibraryPlugin"
            implementationClass = "app.compositeBuild.plugin.AppLibraryPlugin"
    }

    plugins.register("appComponentPlugin") {
            id = "appComponentPlugin"
            implementationClass = "app.compositeBuild.plugin.AppComponentPlugin"
    }

    plugins.register("appMainApplicationPlugin") {
            id = "appMainApplicationPlugin"
            implementationClass = "app.compositeBuild.plugin.AppMainApplicationPlugin"
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.0")
    implementation(kotlin("gradle-plugin", "1.6.10"))
}