package com.teamhy2.ranking.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.ranking.RankingRoute
import com.teamhy2.ranking.navigation.Ranking.ROUTE

fun NavController.navigateToRanking() {
    navigate(ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.rankingScreen() {
    composable(route = ROUTE) {
        RankingRoute()
    }
}

object Ranking {
    const val ROUTE = "ranking"
}
