plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.record.data"
}

dependencies {
    implementation(projects.record.domain)
    implementation(projects.core.remote)

    implementation(projects.calendar.domain)
}
