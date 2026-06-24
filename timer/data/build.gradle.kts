plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.timer.data"
}

dependencies {
    implementation(projects.timer.domain)
    implementation(projects.core.remote)
    implementation(libs.datastore.preferences)
}
