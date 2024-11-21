plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.record.presentation"
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.recordDomain)

    implementation(projects.calendarPresentation)
    implementation(projects.calendarDomain)
    implementation(projects.core.tracker)
}
