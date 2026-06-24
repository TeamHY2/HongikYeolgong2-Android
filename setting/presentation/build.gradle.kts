plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.setting.presentation"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.setting.domain)
    implementation(projects.user.domain)
    implementation(libs.coil)
    implementation(projects.core.tracker)
    implementation(projects.onboarding.presentation)
    implementation(projects.onboarding.domain)
}
