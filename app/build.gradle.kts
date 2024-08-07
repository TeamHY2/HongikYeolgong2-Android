plugins {
    id("hongikyeolgong2.android.application")
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

    implementation(projects.settingPresentation)
    implementation(projects.settingData)
    implementation(projects.settingDomain)

    implementation(projects.core.designsystem)
}
