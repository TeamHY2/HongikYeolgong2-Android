plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.notification.data"
}

dependencies {
    implementation(projects.notification.domain)
    implementation(projects.core.remote)

    implementation(libs.datastore.preferences)
}
