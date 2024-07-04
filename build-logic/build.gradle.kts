plugins {
	id("java-library")
	alias(libs.plugins.jetbrains.kotlin.jvm)
	`kotlin-dsl`
	`kotlin-dsl-precompiled-script-plugins`
}

dependencies {
	implementation(libs.android.gradlePlugin)
	implementation(libs.kotlin.gradlePlugin)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}
