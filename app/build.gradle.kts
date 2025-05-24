plugins {
    id("hongikyeolgong2.android.application")
    alias(libs.plugins.google.services)
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.android.application)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.teamhy2.hongikyeolgong2"

    defaultConfig {
        applicationId = "com.teamhy2.hongikyeolgong2"
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    buildTypes {
        debug {
            isDebuggable = true
            manifestPlaceholders.putAll(
                mapOf(
                    "appIcon" to "@mipmap/ic_app_logo_debug",
                    "roundIcon" to "@mipmap/ic_app_logo_debug_round",
                ),
            )
        }

        release {
            isDebuggable = false
            manifestPlaceholders.putAll(
                mapOf(
                    "appIcon" to "@mipmap/ic_app_logo",
                    "roundIcon" to "@mipmap/ic_app_logo_round",
                ),
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.configureEach {
    if (name.contains("baselineProfile") && System.getenv("CI") == "true") {
        enabled = false
    }
}

dependencies {
    implementation(projects.core.notification)
    implementation(projects.core.designsystem)
    implementation(projects.core.remote)
    implementation(projects.core.auth)
    implementation(projects.core.fcm)

    implementation(projects.mainPresentation)
    implementation(projects.mainData)
    implementation(projects.mainDomain)

    implementation(projects.onboardingPresentation)
    implementation(projects.onboardingData)
    implementation(projects.onboardingDomain)

    implementation(projects.settingPresentation)
    implementation(projects.settingData)
    implementation(projects.settingDomain)

    implementation(projects.timerPresentation)
    implementation(projects.timerData)
    implementation(projects.timerDomain)

    implementation(projects.rankingDomain)
    implementation(projects.rankingData)
    implementation(projects.rankingPresentation)

    implementation(projects.recordDomain)
    implementation(projects.recordData)
    implementation(projects.recordPresentation)

    implementation(projects.userData)
    implementation(projects.userDomain)

    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
}
