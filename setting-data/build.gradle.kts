plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.setting.data"
}

dependencies {

    implementation(projects.settingDomain)

    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
