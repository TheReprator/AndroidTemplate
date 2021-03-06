enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }
}
includeBuild("compositeBuild")

include(
    ":app",
    ":base",
    ":base-android",
    ":navigation",
    ":workTask",
    ":appModules:moduleA",
    ":appModules:location",
    ":appModules:settings"
)
rootProject.buildFileName = "build.gradle.kts"
rootProject.name = "AndroidTemplate"
