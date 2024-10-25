package com.teamhy2.record.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.record.RecordRoute
import com.teamhy2.record.navigation.Ranking.ROUTE

fun NavController.navigateToRecord() {
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.recordScreen() {
    composable(route = ROUTE) {
        RecordRoute()
    }
}

object Ranking {
    const val ROUTE = "record"
}
