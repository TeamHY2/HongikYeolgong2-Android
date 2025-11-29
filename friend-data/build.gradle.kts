plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.friend.data"
}

dependencies {
    implementation(projects.friendDomain)
    implementation(projects.core.remote)
    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
}
