plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	id("hongikyeolgong2.android.compose")
}

android {
	namespace = "com.teamhy2.designsystem"
	compileSdk = 34

	defaultConfig {
		minSdk = 30
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
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
	kotlinOptions {
		jvmTarget = "17"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}
