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

include(
    ":app",
    ":base",
    ":base-android",
    ":navigation",
    ":workTask",
    ":moduleNotification",
    ":appModules:moduleA",
    ":appModules:location",
    ":appModules:notification",
    ":appModules:settings"
)

rootProject.buildFileName = "build.gradle.kts"
rootProject.name = "AndroidTemplate"
