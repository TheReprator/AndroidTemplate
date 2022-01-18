import app.template.buildsrc.AppDependencyUpdates
import app.template.buildsrc.ReleaseType
import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("version.gradle") apply false
    id(Libs.Plugins.dokka) version (Libs.Versions.dokka)
    id(Libs.Plugins.spotless) version Libs.Versions.spotless

    id(Libs.Plugins.detekt) version Libs.Versions.detekt
    id(Libs.Plugins.dependencyUpdateVersions) version Libs.Versions.dependencyUpdate
}

buildscript {

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Libs.androidGradlePlugin)
        classpath(Libs.Kotlin.gradlePlugin)
        classpath(Libs.AndroidX.Navigation.navigationPlugin)
        classpath(Libs.DaggerHilt.classPath)

        classpath(Libs.crashlytics)
        classpath(Libs.Google.gmsGoogleServices)

        classpath(Libs.TestDependencies.Junit5.classPath)
    }
}

subprojects {

    plugins.apply(Libs.Plugins.detekt)

    plugins.apply(Libs.Plugins.dokka)
    plugins.apply(Libs.Plugins.spotless)

    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(Libs.Versions.ktlint)
            licenseHeaderFile("${project.rootProject.projectDir}/config/spotless/copyright.kt")
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }

    tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
        dokkaSourceSets.configureEach {
            noAndroidSdkLink.set(true)
            suppressInheritedMembers.set(true)
        }
    }

    detekt {
        description = "Runs over whole code base without the starting overhead for each module."
        parallel = true
        buildUponDefaultConfig = true
        autoCorrect = true
        config.setFrom(files("${rootProject.rootDir}/config/detekt/detekt.yml"))

        reports {
            html.required.set(true)
            html.outputLocation.set(file("${rootProject.rootDir}/reports/detekt/detekt.html"))
        }
    }

    dependencies {
        detektPlugins(Libs.detekt)
    }
}

/*Document Generation for all modules*/
tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(file("$rootDir/reports/dokka"))
}

/*
Generate dependency graph for app in terms of modules
* */
apply(from = file("$rootDir/gradle/dependencyGraph.gradle"))
project.rootProject.allprojects {
    apply(plugin = "project-report")

    this.task("allDependencies", DependencyReportTask::class) {

        outputFile = file("$rootDir/reports/dependencies.txt")

        evaluationDependsOnChildren()
        this.setRenderer(org.gradle.api.tasks.diagnostics.internal.dependencies.AsciiDependencyReportRenderer())
    }
}

//Task added to know the updated depencecy
tasks.withType<DependencyUpdatesTask> {

    rejectVersionIf {
        val current = AppDependencyUpdates.versionToRelease(currentVersion)
        // If we're using a SNAPSHOT, ignore since we must be doing so for a reason.
        if (current == ReleaseType.SNAPSHOT) return@rejectVersionIf true

        // Otherwise we reject if the candidate is more 'unstable' than our version
        val candidate = AppDependencyUpdates.versionToRelease(candidate.version)
        return@rejectVersionIf candidate.isLessStableThan(current)
    }

    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "$rootDir/reports/dependencyUpdates"
    reportfileName = "report"
}

/*
Junit5 Configuration for all modules
* */
subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            lifecycle {
                events =
                    mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                showStandardStreams = true
            }
            info.events = lifecycle.events
            info.exceptionFormat = lifecycle.exceptionFormat
        }

        val failedTests = mutableListOf<TestDescriptor>()
        val skippedTests = mutableListOf<TestDescriptor>()

        // See https://github.com/gradle/kotlin-dsl/issues/836
        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                when (result.resultType) {
                    TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                    TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                    else -> Unit
                }
            }

            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                if (suite.parent == null) { // root suite
                    logger.lifecycle("----")
                    logger.lifecycle("Test result: ${result.resultType}")
                    logger.lifecycle(
                        "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped"
                    )
                    failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
                    skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
                }
            }

            private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
                logger.lifecycle(subject)
                forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
            }

            private fun TestDescriptor.displayName() =
                parent?.let { "${it.name} - $name" } ?: "$name"
        })
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=Kotlin.RequiresOptIn"
}