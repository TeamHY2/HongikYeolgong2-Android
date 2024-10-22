package com.teamhy2.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.teamhy2.feature.main.navigation.mainScreen
import com.teamhy2.feature.main.navigation.popUpToMain
import com.teamhy2.feature.main.webviews.inquiry.navigation.inquiryScreen
import com.teamhy2.feature.main.webviews.inquiry.navigation.navigateToInquiry
import com.teamhy2.feature.setting.presentation.navigation.navigateToSetting
import com.teamhy2.feature.setting.presentation.navigation.settingScreen
import com.teamhy2.hongikyeolgong2.notification.PushText
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.onboardingScreen
import com.teamhy2.onboarding.navigation.popUpToOnboarding
import com.teamhy2.onboarding.navigation.popUpToSignUp
import com.teamhy2.onboarding.navigation.signUpScreen

@Composable
fun HY2NavHost(
    navController: NavHostController,
    urls: Map<String, String>,
    onSendNotification: (PushText) -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = Onboarding.ROUTE,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ) {
            onboardingScreen(
                onUserSignedIn = navController::popUpToMain,
                onGuestSignedIn = navController::popUpToSignUp,
            )

            signUpScreen(
                onSignUpButtonClicked = navController::popUpToMain,
            )

            mainScreen(
                seatingChartUrl = urls["seatingChart"] ?: "",
                onSettingClick = navController::navigateToSetting,
                onSendNotification = onSendNotification,
            )

            inquiryScreen(
                url = urls["inquiry"] ?: "",
                onCloseButtonClick = navController::popBackStack,
            )

            settingScreen(
                noticeUrl = urls["notice"] ?: "",
                onBackButtonClick = navController::popBackStack,
                onInquiryClick = navController::navigateToInquiry,
                onLogoutOrWithdrawComplete = {
                    onLogoutOrWithdrawComplete()
                    navController::popUpToOnboarding
                },
            )
        }
    }
}
