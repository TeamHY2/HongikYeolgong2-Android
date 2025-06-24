plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.core.testing"
}

dependencies {
    api(libs.junit4)
    api(libs.junit.vintage.engine)
    api(libs.kotlin.test)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.coroutines.test)
    api(libs.androidx.compose.ui.test)
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.runner)
    implementation(projects.core.designsystem)
}
