plugins {
    id("hongikyeolgong2.android.feature")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.main.presentation"
}

dependencies {
    // TODO: 사용하는 feature 모듈의 의존성 추가
    // ex) implementation(projects.feature.home)
    implementation(projects.mainDomain)

    implementation(projects.calendarPresentation)
    implementation(projects.calendarDomain)

    implementation(projects.onboardingPresentation)
    implementation(projects.onboardingDomain)

    implementation(projects.timerPresentation)
    implementation(projects.timerDomain)

    implementation(projects.settingPresentation)
    implementation(projects.settingDomain)

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

    implementation(libs.firebase.ui.auth)
}
