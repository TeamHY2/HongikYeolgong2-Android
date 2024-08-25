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
import com.teamhy2.feature.main.navigation.popUpToMain
import com.teamhy2.feature.main.webviews.inquiry.navigation.inquiryScreen
import com.teamhy2.feature.main.webviews.inquiry.navigation.navigateToInquiry
import com.teamhy2.feature.main.webviews.notice.navigation.navigateToNotice
import com.teamhy2.feature.main.webviews.notice.navigation.noticeScreen
import com.teamhy2.feature.main.webviews.seatingChart.navigation.navigateToSeatingChart
import com.teamhy2.feature.main.webviews.seatingChart.navigation.seatingChartScreen
import com.teamhy2.feature.setting.presentation.navigation.navigateToSetting
import com.teamhy2.feature.setting.presentation.navigation.settingScreen
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.navigateToSignUp
import com.teamhy2.onboarding.navigation.onboardingScreen
import com.teamhy2.onboarding.navigation.signUpScreen

@Composable
fun HY2NavHost(
    navController: NavHostController,
    urls: Map<String, String>,
    googleSignIn: () -> Unit,
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
            onboardingScreen(
                onGoogleLoginClick = googleSignIn,
                onGoogleLoginDone = navController::navigateToSignUp,
            )

            signUpScreen(
                onSignUpButtonClicked = navController::popUpToMain,
            )

            mainScreen(
                onSettingClick = navController::navigateToSetting,
                onSeatingChartClick = navController::navigateToSeatingChart,
            )

            seatingChartScreen(
                url = urls["seatingChart"] ?: "",
                onCloseButtonClick = navController::popBackStack,
            )

            noticeScreen(
                url = urls["notice"] ?: "",
                onCloseButtonClick = navController::popBackStack,
            )

            inquiryScreen(
                url = urls["inquiry"] ?: "",
                onCloseButtonClick = navController::popBackStack,
            )

            settingScreen(
                onBackButtonClick = navController::popBackStack,
                onNoticeClick = navController::navigateToNotice,
                onInquiryClick = navController::navigateToInquiry,
            )
        }
    }
}
