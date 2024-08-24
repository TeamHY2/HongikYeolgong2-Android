plugins {
    id("hongikyeolgong2.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2"

    defaultConfig {
        applicationId = "com.teamhy2.hongikyeolgong2"
        versionCode = 1
        versionName = "1.0"
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
    implementation(projects.timerDomain)

    implementation(projects.core.designsystem)
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.firebase.analytics)
}
