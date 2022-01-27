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
    alias(libs.plugins.dokka)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.gradleDependencyUpdate)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.pluginGradle)
        classpath(libs.kotlin.pluginGradle)
        classpath(libs.hilt.pluginGradle)
        classpath(libs.junit5.pluginGradle)
        classpath(libs.androidx.navigation.safeArgument)

        classpath(libs.google.crashlyticsGradle)
        classpath(libs.google.gmsGoogleServices)
    }
}

subprojects {

    plugins.apply("io.gitlab.arturbosch.detekt")
    plugins.apply("org.jetbrains.dokka")
    plugins.apply("com.diffplug.spotless")

    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(libs.versions.ktlint.get())
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
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
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
