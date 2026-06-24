plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.ranking.data"
}

dependencies {
    implementation(projects.ranking.domain)
    implementation(projects.core.remote)
}
