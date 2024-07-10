package com.teamhy2.app

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid() {
	// Plugins
	pluginManager.apply("org.jetbrains.kotlin.android")

	// Android
	androidExtension.apply {
		compileSdk = 34
		defaultConfig {
			minSdk = 30
			testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		}

		buildTypes {
			getByName("release") {
				isMinifyEnabled = false
				proguardFiles(
					getDefaultProguardFile("proguard-android-optimize.txt"),
					"proguard-rules.pro",
				)
			}
		}
		compileOptions {
			sourceCompatibility = JavaVersion.VERSION_17
			targetCompatibility = JavaVersion.VERSION_17
		}
		packaging {
			resources {
				excludes += "/META-INF/{AL2.0,LGPL2.1}"
			}
		}
	}

	configureKotlin()
}

internal fun Project.configureKotlin() {
	tasks.withType<KotlinCompile>().configureEach {
		kotlinOptions {
			jvmTarget = JavaVersion.VERSION_17.toString()
		}
	}
}
