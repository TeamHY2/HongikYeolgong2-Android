plugins {
    id("hongikyeolgong2.android.feature")
}

android {
    namespace = "com.teamhy2.hongikyeolgong2.main.presentation"
}

dependencies {

    implementation(projects.main.domain)

    implementation(projects.core.notification)
    implementation(projects.core.remote)
    implementation(projects.core.tracker)

    implementation(projects.calendar.presentation)
    implementation(projects.calendar.domain)

    implementation(projects.onboarding.presentation)
    implementation(projects.onboarding.domain)

    implementation(projects.timer.presentation)
    implementation(projects.timer.domain)

    implementation(projects.setting.presentation)
    implementation(projects.setting.domain)

    implementation(projects.ranking.presentation)
    implementation(projects.ranking.domain)

    implementation(projects.record.domain)
    implementation(projects.record.presentation)

    implementation(projects.friend.domain)
    implementation(projects.friend.presentation)

    implementation(projects.user.domain)

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

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.coil)
    implementation(libs.kotlinx.immutable.collection)
}
