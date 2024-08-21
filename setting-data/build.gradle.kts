plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.setting.data"
}

dependencies {
    implementation(projects.settingDomain)
    implementation(libs.datastore.preferences)
}
