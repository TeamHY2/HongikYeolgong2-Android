plugins {
    id("hongikyeolgong2.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2"

    defaultConfig {
        applicationId = "com.teamhy2.hongikyeolgong2"
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
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

    implementation(projects.core.designsystem)
    implementation(projects.core.remote)
    implementation(projects.core.auth)

    implementation(projects.app.notification)
    implementation(projects.app.auth)

    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.accompanist.permissions)
}
