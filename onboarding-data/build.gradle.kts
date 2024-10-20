plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.onboarding.data"
}

dependencies {
    implementation(projects.onboardingDomain)
    implementation(projects.core.remote)
    implementation(projects.core.auth)

    implementation(libs.firebase.firestore.ktx)
}
