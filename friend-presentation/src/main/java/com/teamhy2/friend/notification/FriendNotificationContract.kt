package com.teamhy2.friend.notification

import com.teamhy2.friend.domain.model.FriendNotification
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FriendNotificationUiState(
    val isLoading: Boolean = false,
    val notifications: ImmutableList<FriendNotification> = persistentListOf(),
)

sealed interface FriendNotificationEffect {
    data class ShowSnackBar(val message: String) : FriendNotificationEffect

    data object NavigateBack : FriendNotificationEffect
}
