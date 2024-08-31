plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.notification"
}

dependencies {
    implementation(projects.settingDomain)
    implementation(projects.settingData)
}
