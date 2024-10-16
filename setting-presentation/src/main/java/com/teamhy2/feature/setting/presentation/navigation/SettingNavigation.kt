package com.teamhy2.feature.setting.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.setting.presentation.SettingRoute
import com.teamhy2.feature.setting.presentation.navigation.Setting.ROUTE

fun NavController.navigateToSetting() {
    navigate(ROUTE)
}

fun NavGraphBuilder.settingScreen(
    noticeUrl: String,
    onInquiryClick: () -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
) {
    composable(route = ROUTE) {
        SettingRoute(
            noticeUrl = noticeUrl,
            onInquiryClick = onInquiryClick,
            onLogoutOrWithdrawComplete = onLogoutOrWithdrawComplete,
        )
    }
}

object Setting {
    const val ROUTE = "setting"
}
