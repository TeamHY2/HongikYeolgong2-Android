plugins {
    id("hongikyeolgong2.android.library")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.record.data"
}

dependencies {
    implementation(projects.recordDomain)
    implementation(projects.core.remote)

    implementation(projects.calendarDomain)
}
