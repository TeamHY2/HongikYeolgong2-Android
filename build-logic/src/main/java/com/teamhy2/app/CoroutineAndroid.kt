package com.teamhy2.app

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureCoroutineAndroid() {
	val libs = extensions.libs
	dependencies {
		"implementation"(libs.findLibrary("coroutines.android").get())
	}
}

fun Project.configureCoroutineKotlin() {
	val libs = extensions.libs
	dependencies {
		"implementation"(libs.findLibrary("coroutines.core").get())
		"testImplementation"(libs.findLibrary("coroutines.test").get())
	}
}
