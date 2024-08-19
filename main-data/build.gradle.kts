plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.main.data"
}

dependencies {
    implementation(projects.mainDomain)

    implementation(libs.firebase.firestore.ktx)
}
