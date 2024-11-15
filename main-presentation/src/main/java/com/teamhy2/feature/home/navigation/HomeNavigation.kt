package com.teamhy2.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.home.HomeRoute
import com.teamhy2.feature.home.navigation.Home.ROUTE

fun NavController.navigateToHome() {
    navigate(ROUTE)
}

fun NavController.popUpToHome() {
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(seatingChartUrl: String) {
    composable(route = ROUTE) {
        HomeRoute(
            seatingChartUrl = seatingChartUrl,
        )
    }
}

object Home {
    const val ROUTE = "home"
}
