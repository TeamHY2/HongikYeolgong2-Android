plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.friend.data"
}

dependencies {
    implementation(projects.friend.domain)
    implementation(projects.core.remote)
    implementation(projects.main.domain)
}
