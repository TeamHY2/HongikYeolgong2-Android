plugins {
    id("hongikyeolgong2.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.main.data"
}

dependencies {
    implementation(projects.main.domain)
    implementation(projects.core.remote)

    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.config.ktx)
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
