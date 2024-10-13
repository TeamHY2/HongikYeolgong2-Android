import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("hongikyeolgong2.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.benenfeldt.remote"

    defaultConfig {
        buildConfigField("String", "BASE_URL", getApiKey("BASE_URL"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.adapters.result)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
