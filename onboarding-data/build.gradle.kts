plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.onboarding.data"
}

dependencies {
    implementation(projects.onboardingDomain)
    implementation(projects.app.auth)
    implementation(projects.core.remote)

    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.ui.auth)
}
