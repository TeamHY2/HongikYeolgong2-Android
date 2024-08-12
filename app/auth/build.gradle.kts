plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.auth"
}

dependencies {
    implementation(libs.firebase.auth)
}
