package com.teamhy2.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.home.HomeRoute
import com.teamhy2.feature.home.navigation.Home.ROUTE
import com.teamhy2.hongikyeolgong2.notification.PushText

fun NavController.navigateToHome() {
    navigate(ROUTE)
}

fun NavController.popUpToHome() {
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.homeScreen(
    seatingChartUrl: String,
    onSendNotification: (PushText) -> Unit,
) {
    composable(route = ROUTE) {
        HomeRoute(
            seatingChartUrl = seatingChartUrl,
            onSendNotification = onSendNotification,
        )
    }
}

object Home {
    const val ROUTE = "home"
}
