plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.friend.presentation"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.friend.domain)
    implementation(projects.notification.domain)

    implementation(libs.kotlinx.immutable.collection)
}
