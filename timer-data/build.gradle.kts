plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.timer.data"
}

dependencies {
    implementation(projects.timerDomain)

    implementation(libs.firebase.firestore.ktx)
}
