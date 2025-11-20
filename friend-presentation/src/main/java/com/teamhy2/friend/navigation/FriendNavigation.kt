package com.teamhy2.friend.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.friend.FriendRoute

fun NavController.navigateToFriend() {
    navigate(Friend.ROUTE) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.friendScreen() {
    composable(route = Friend.ROUTE) {
        FriendRoute()
    }
}

object Friend {
    const val ROUTE: String = "friend"
}
