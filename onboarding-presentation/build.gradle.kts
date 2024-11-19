plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.onboarding.presentation"
}

dependencies {
    implementation(projects.onboardingDomain)
    implementation(projects.core.auth)
    implementation(projects.core.remote)

    implementation(projects.userDomain)

    implementation(libs.accompanist.permissions)
}
