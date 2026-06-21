plugins {
    id("hongikyeolgong2.android.library")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.fcm"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
    implementation(projects.user.domain)
}
