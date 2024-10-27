plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.main.data"
}

dependencies {
    implementation(projects.mainDomain)
    implementation(projects.core.remote)

    implementation(libs.firebase.firestore.ktx)
}
