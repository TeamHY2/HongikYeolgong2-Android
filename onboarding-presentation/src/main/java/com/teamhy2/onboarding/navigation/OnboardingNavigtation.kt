package com.teamhy2.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.onboarding.OnboardingRoute
import com.teamhy2.onboarding.navigation.Onboarding.ROUTE

fun NavController.popUpToOnboarding() {
    // back stack screen을 모두 제거하고 이동 합니다.
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.onboardingScreen(
    onGoogleLoginClick: () -> Unit,
    onGoogleLoginDone: () -> Unit,
) {
    composable(route = ROUTE) {
        OnboardingRoute(
            onGoogleLoginClick = onGoogleLoginClick,
            onGoogleLoginDone = onGoogleLoginDone,
        )
    }
}

object Onboarding {
    const val ROUTE = "onboarding"
}
