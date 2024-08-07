plugins {
    id("hongikyeolgong2.android.feature")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.main.presentation"
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // TODO: 사용하는 feature 모듈의 의존성 추가
    // ex) implementation(projects.feature.home)
    implementation(projects.calendarPresentation)
    implementation(projects.calendarDomain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
}
