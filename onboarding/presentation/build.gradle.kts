plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.onboarding.presentation"
}

dependencies {
    implementation(projects.onboarding.domain)
    implementation(projects.core.auth)
    implementation(projects.core.remote)

    implementation(projects.user.domain)

    implementation(libs.accompanist.permissions)
}
