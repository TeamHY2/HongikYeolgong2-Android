plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.record.presentation"
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.record.domain)

    implementation(projects.calendar.presentation)
    implementation(projects.calendar.domain)
    implementation(projects.core.tracker)
}
