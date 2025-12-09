package com.teamhy2.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.teamhy2.feature.home.navigation.homeNavigation
import com.teamhy2.feature.home.navigation.popUpToHome
import com.teamhy2.feature.main.webviews.inquiry.navigation.inquiryScreen
import com.teamhy2.feature.main.webviews.inquiry.navigation.navigateToInquiry
import com.teamhy2.feature.setting.presentation.navigation.settingScreen
import com.teamhy2.friend.navigation.friendScreen
import com.teamhy2.friend.search.navigation.friendSearchScreen
import com.teamhy2.friend.search.navigation.navigateToFriendSearch
import com.teamhy2.onboarding.navigation.Onboarding
import com.teamhy2.onboarding.navigation.onboardingScreen
import com.teamhy2.onboarding.navigation.popUpToOnboarding
import com.teamhy2.onboarding.navigation.popUpToSignUp
import com.teamhy2.onboarding.navigation.signUpScreen
import com.teamhy2.ranking.navigation.rankingScreen
import com.teamhy2.record.navigation.recordScreen

@Composable
fun HY2NavHost(
    navController: NavHostController,
    urls: Map<String, String>,
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
                onUserSignedIn = navController::popUpToHome,
                onGuestSignedIn = navController::popUpToSignUp,
            )

            signUpScreen(
                onSignUpButtonClicked = navController::popUpToHome,
            )

            homeNavigation(
                navController = navController,
                urls = urls,
            )

            friendScreen(
                onAddFriendClick = navController::navigateToFriendSearch,
            )

            friendSearchScreen(
                onBackClick = navController::popBackStack,
            )

            rankingScreen()

            recordScreen()

            settingScreen(
                noticeUrl = urls["notice"] ?: "",
                onInquiryClick = navController::navigateToInquiry,
                onLogoutOrWithdrawComplete = {
                    onLogoutOrWithdrawComplete()
                    navController.popUpToOnboarding()
                },
            )

            inquiryScreen(
                url = urls["inquiry"] ?: "",
                onCloseButtonClick = navController::popBackStack,
            )
        }
    }
}
