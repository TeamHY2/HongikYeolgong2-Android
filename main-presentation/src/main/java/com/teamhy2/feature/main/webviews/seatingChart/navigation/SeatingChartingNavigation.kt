package com.teamhy2.feature.main.webviews.seatingChart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.feature.main.webviews.seatingChart.SeatingChartRoute
import com.teamhy2.feature.main.webviews.seatingChart.navigation.SeatingChart.ROUTE

fun NavController.navigateToSeatingChart() {
    navigate(ROUTE)
}

fun NavGraphBuilder.seatingChartScreen(
    url: String,
    onCloseButtonClick: () -> Unit,
) {
    composable(route = ROUTE) {
        SeatingChartRoute(
            url = url,
            onCloseButtonClick = onCloseButtonClick,
        )
    }
}

object SeatingChart {
    const val ROUTE = "seatingChart"
}
