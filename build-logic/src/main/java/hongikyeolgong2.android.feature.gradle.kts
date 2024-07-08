import com.teamhy2.app.configureHiltAndroid
import com.teamhy2.app.libs

plugins {
	id("hongikyeolgong2.android.library")
	id("hongikyeolgong2.android.compose")
}

android {
	defaultConfig {
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
}

configureHiltAndroid()

dependencies {
	// TODO: 사용하는 core 모듈의 의존성 추가
	implementation(project(":core:designsystem"))

	val libs = project.extensions.libs
	implementation(libs.findLibrary("hilt.navigation.compose").get())
	implementation(libs.findLibrary("androidx.compose.navigation").get())
	androidTestImplementation(libs.findLibrary("androidx.compose.navigation.test").get())

	implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
	implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
}
