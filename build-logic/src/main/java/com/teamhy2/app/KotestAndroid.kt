package com.teamhy2.app

import org.gradle.api.Project

internal fun Project.configureKotestAndroid() {
	configureKotest()
	configureJUnitAndroid()
}

internal fun Project.configureJUnitAndroid() {
	androidExtension.apply {
		testOptions {
			unitTests.all { it.useJUnitPlatform() }
		}
	}
}
