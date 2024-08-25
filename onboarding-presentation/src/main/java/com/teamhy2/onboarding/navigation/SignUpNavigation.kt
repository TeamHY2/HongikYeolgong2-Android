package com.teamhy2.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.onboarding.SignUpRoute
import com.teamhy2.onboarding.navigation.SignUp.ROUTE

fun NavController.navigateToSignUp() {
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.signUpScreen(onSignUpButtonClicked: () -> Unit) {
    composable(route = ROUTE) {
        SignUpRoute(onSignUpButtonClicked = onSignUpButtonClicked)
    }
}

object SignUp {
    const val ROUTE = "signUp"
}
