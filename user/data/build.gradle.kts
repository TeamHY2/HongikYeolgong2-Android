plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.user.data"
}

dependencies {
    implementation(projects.core.remote)
    implementation(projects.core.auth)

    implementation(projects.user.domain)
}
