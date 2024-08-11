plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.onboarding.data"
}

dependencies {
    implementation(projects.onboardingDomain)
}
