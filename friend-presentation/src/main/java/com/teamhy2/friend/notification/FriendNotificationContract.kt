package com.teamhy2.friend.notification

import com.teamhy2.friend.domain.model.FriendNotification

data class FriendNotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<FriendNotification> = emptyList(),
)

sealed interface FriendNotificationEffect {
    data class ShowSnackBar(val message: String) : FriendNotificationEffect

    data object NavigateBack : FriendNotificationEffect
}
