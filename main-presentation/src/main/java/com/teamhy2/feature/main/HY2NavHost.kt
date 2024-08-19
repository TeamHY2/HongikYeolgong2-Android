package com.teamhy2.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.feature.main.navigation.mainScreen
import com.teamhy2.feature.main.navigation.navigateToMain
import com.teamhy2.feature.setting.presentation.navigation.navigateToSetting
import com.teamhy2.feature.setting.presentation.navigation.settingScreen
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.onboardingScreen

@Composable
fun HY2NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Onboarding.ROUTE,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ) {
            // TODO 소셜 로그인이 성공한 뒤 스크린 연결 필요
            onboardingScreen(
                onGoogleLoginClick = navController::navigateToMain,
            )

            // TODO onSeatingChartClick 스크린 연결 필요
            mainScreen(
                onSettingClick = navController::navigateToSetting,
                onSeatingChartClick = navController::navigateToSetting,
            )

            // TODO onNoticeClick, onInquiryClick 스크린 연결 필요
            settingScreen(
                onBackButtonClick = navController::popBackStack,
                onNoticeClick = navController::popBackStack,
                onInquiryClick = navController::popBackStack,
            )
        }
    }
}
