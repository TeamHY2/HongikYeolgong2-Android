package com.teamhy2.friend.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teamhy2.friend.notification.FriendNotificationRoute

fun NavController.navigateToFriendNotification() {
    navigate(FriendNotification.ROUTE)
}

fun NavGraphBuilder.friendNotificationScreen(onBackClick: () -> Unit) {
    composable(route = FriendNotification.ROUTE) {
        FriendNotificationRoute(onBackClick = onBackClick)
    }
}

object FriendNotification {
    const val ROUTE: String = "friend/notification"
}
