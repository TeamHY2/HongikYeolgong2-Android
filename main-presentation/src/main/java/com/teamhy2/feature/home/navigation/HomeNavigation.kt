package com.teamhy2.feature.home.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.teamhy2.feature.focus.FocusModeRoute
import com.teamhy2.feature.home.HomeRoute
import com.teamhy2.feature.home.HomeViewModel
import com.teamhy2.hongikyeolgong2.timer.presentation.TimerViewModel

fun NavController.navigateToHome() {
    navigate(Home.ROUTE)
}

fun NavController.popUpToHome() {
    navigate(Home.ROUTE) {
        popUpTo(Home.ROUTE)
        launchSingleTop = true
    }
}

fun NavController.navigateToFocusMode() {
    navigate(FocusMode.ROUTE)
}

fun NavGraphBuilder.homeNavigation(
    navController: NavHostController,
    urls: Map<String, String>,
) {
    navigation(startDestination = Home.ROUTE, route = Home.NAVIGATION_ROUTE) {
        composable(route = Home.ROUTE) { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    try {
                        navController.getBackStackEntry(Home.NAVIGATION_ROUTE)
                    } catch (_: IllegalArgumentException) {
                        null
                    }
                }

            if (parentEntry != null) {
                val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)
                val timerViewModel: TimerViewModel = hiltViewModel(parentEntry)

                HomeRoute(
                    seatingChartUrl = urls["seatingChart"] ?: "",
                    homeViewModel = homeViewModel,
                    timerViewModel = timerViewModel,
                )
            }
        }

        composable(route = FocusMode.ROUTE) { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    try {
                        navController.getBackStackEntry(Home.NAVIGATION_ROUTE)
                    } catch (_: IllegalArgumentException) {
                        null
                    }
                }

            if (parentEntry != null) {
                val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)
                val timerViewModel: TimerViewModel = hiltViewModel(parentEntry)

                FocusModeRoute(
                    homeViewModel = homeViewModel,
                    timerViewModel = timerViewModel,
                )
            }
        }
    }
}

object Home {
    const val ROUTE = "home"
    const val NAVIGATION_ROUTE = "home_graph"
}

object FocusMode {
    const val ROUTE = "focus_mode"
}
