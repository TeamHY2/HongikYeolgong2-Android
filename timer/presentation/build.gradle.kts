plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.timer.presentation"
}

dependencies {
    implementation(projects.timer.domain)
    implementation(projects.main.domain)
    implementation(projects.core.notification)
    implementation(libs.androidx.lifecycle.service)
}
