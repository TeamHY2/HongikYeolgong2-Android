plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.ranking.data"
}

dependencies {
    implementation(projects.rankingDomain)
    implementation(projects.core.remote)
}
