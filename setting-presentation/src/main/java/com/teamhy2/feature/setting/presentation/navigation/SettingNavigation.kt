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
    onBackButtonClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onInquiryClick: () -> Unit,
) {
    composable(route = ROUTE) {
        SettingRoute(
            onBackButtonClick = onBackButtonClick,
            onNoticeClick = onNoticeClick,
            onInquiryClick = onInquiryClick,
        )
    }
}

object Setting {
    const val ROUTE = "setting"
}
