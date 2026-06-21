package com.teamhy2.friend.data.repository

import com.benenfeldt.remote.api.FriendService
import com.benenfeldt.remote.api.NotificationService
import com.benenfeldt.remote.dto.UpdateFriendStatusRequest
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.friend.data.mapper.toDomain
import com.teamhy2.friend.data.mapper.toRemote
import com.teamhy2.friend.domain.model.FriendNotification
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.domain.repository.FriendNotificationRepository
import javax.inject.Inject

class FriendNotificationRepositoryImpl @Inject constructor(
    private val notificationService: NotificationService,
    private val friendService: FriendService,
) : FriendNotificationRepository {
    override suspend fun fetchNotifications(): Result<List<FriendNotification>> =
        notificationService.getNotifications().toResult { response ->
            response.data.map { it.toDomain() }
        }

    override suspend fun updateFriendRequest(
        notificationId: Long,
        friendId: Long,
        senderId: Long,
        status: FriendStatus,
    ): Result<FriendStatus> =
        friendService.updateFriendStatus(
            UpdateFriendStatusRequest(
                notificationId = notificationId,
                friendId = friendId,
                senderId = senderId,
                friendStatus = status.toRemote(),
            ),
        ).toResult { it.data.friendStatus.toDomain() }
}
