// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.jetbrains.kotlin.android) apply false
	alias(libs.plugins.ktlint)
}

allprojects {
	apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)

	ktlint {
		android = true
	}
}
