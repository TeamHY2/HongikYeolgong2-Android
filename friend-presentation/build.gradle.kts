plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.friend.presentation"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.friendDomain)
}
