plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.setting.presentation"
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.settingDomain)
}
