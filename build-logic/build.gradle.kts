plugins {
	`kotlin-dsl`
	`kotlin-dsl-precompiled-script-plugins`
}

dependencies {
	implementation(libs.android.gradlePlugin)
	implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
	plugins {
		register("androidHilt") {
			id = "hongikyeolgong2.android.hilt"
			implementationClass = "com.teamhy2.app.HiltAndroidPlugin"
		}
		register("kotlinHilt") {
			id = "hongikyeolgong2.kotlin.hilt"
			implementationClass = "com.teamhy2.app.HiltKotlinPlugin"
		}
	}
}
