plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.timer.presentation"
}

dependencies {
    implementation(projects.timerDomain)
}
