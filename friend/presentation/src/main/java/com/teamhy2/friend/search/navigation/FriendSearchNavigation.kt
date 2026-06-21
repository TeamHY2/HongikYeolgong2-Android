package com.teamhy2.friend.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.friend.search.FriendSearchRoute

fun NavController.navigateToFriendSearch() {
    navigate(FriendSearch.ROUTE)
}

fun NavGraphBuilder.friendSearchScreen(onBackClick: () -> Unit) {
    composable(route = FriendSearch.ROUTE) {
        FriendSearchRoute(
            onBackClick = onBackClick,
        )
    }
}

object FriendSearch {
    const val ROUTE: String = "friend/search"
}
