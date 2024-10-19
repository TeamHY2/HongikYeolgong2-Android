import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("hongikyeolgong2.android.feature")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.teamhy2.core.auth"
    defaultConfig {
        buildConfigField("String", "WEB_CLIENT_ID", getApiKey("WEB_CLIENT_ID"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.credentials)
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
