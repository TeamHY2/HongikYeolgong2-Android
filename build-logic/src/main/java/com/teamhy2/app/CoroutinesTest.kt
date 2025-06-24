package com.teamhy2.app

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCoroutinesTest() {
    configureJUnit()

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("coroutines-core").get())
        "testImplementation"(libs.findLibrary("turbine").get())

        "testImplementation"(libs.findLibrary("test-junit5").get())
        "testRuntimeOnly"(libs.findLibrary("test-junit5-engine").get())
    }
}
