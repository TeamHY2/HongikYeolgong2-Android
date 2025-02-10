import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("hongikyeolgong2.android.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.benenfeldt.remote"

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", getApiKey("DEBUG_BASE_URL"))
        }

        release {
            buildConfigField("String", "BASE_URL", getApiKey("RELEASE_BASE_URL"))
        }
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

    implementation(libs.datastore.preferences)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
