package com.teamhy2.feature.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.main.MainRoute
import com.teamhy2.feature.main.navigation.Main.ROUTE
import com.teamhy2.hongikyeolgong2.notification.PushText

fun NavController.navigateToMain() {
    navigate(ROUTE)
}

fun NavController.popUpToMain() {
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.mainScreen(
    onSettingClick: () -> Unit,
    onSeatingChartClick: () -> Unit,
    onSendNotification: (PushText) -> Unit,
    onBackgroundChanged: () -> Unit,
) {
    onBackgroundChanged()
    composable(route = ROUTE) {
        MainRoute(
            onSettingClick = onSettingClick,
            onSeatingChartClick = onSeatingChartClick,
            onSendNotification = onSendNotification,
        )
    }
}

object Main {
    const val ROUTE = "main"
}
