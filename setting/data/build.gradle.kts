plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.setting.data"
}

dependencies {
    implementation(projects.setting.domain)
    implementation(libs.datastore.preferences)
}
