package com.teamhy2.feature.setting.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.setting.presentation.ProfileModificationRoute
import com.teamhy2.feature.setting.presentation.SettingRoute
import com.teamhy2.feature.setting.presentation.navigation.Setting.ROUTE

fun NavController.navigateToSetting() {
    navigate(ROUTE)
}

fun NavController.navigateToProfileModification() {
    navigate(Setting.PROFILE_MODIFICATION_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.settingScreen(
    noticeUrl: String,
    onNavigateToInquiry: () -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
) {
    composable(route = ROUTE) {
        SettingRoute(
            noticeUrl = noticeUrl,
            onNavigateToInquiry = onNavigateToInquiry,
            onLogoutOrWithdrawComplete = onLogoutOrWithdrawComplete,
        )
    }
    composable(route = Setting.PROFILE_MODIFICATION_ROUTE) {
        ProfileModificationRoute()
    }
}

object Setting {
    const val ROUTE = "setting"
    const val PROFILE_MODIFICATION_ROUTE = "profile_modification"
}
