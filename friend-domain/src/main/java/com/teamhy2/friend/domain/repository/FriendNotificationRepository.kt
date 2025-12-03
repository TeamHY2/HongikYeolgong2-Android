package com.teamhy2.friend.domain.repository

import com.teamhy2.friend.domain.model.FriendNotification
import com.teamhy2.friend.domain.model.FriendStatus

interface FriendNotificationRepository {
    suspend fun fetchNotifications(): Result<List<FriendNotification>>

    suspend fun updateFriendRequest(
        notificationId: Long,
        friendId: Long,
        senderId: Long,
        status: FriendStatus,
    ): Result<FriendStatus>
}
