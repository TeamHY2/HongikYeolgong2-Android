plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.notification.data"
}

dependencies {
    implementation(projects.notificationDomain)
    implementation(projects.core.remote)
}
