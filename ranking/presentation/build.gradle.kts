plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.ranking.presentation"
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.ranking.domain)
    implementation(projects.core.tracker)
    implementation(libs.kotlinx.immutable.collection)
}
