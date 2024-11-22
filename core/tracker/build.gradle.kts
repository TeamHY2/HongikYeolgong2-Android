import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.tracker"

    defaultConfig {
        buildConfigField("String", "AMPLITUDE_KEY", getApiKey("AMPLITUDE_KEY"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.amplitude)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
