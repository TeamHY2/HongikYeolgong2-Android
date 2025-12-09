package com.teamhy2.friend.data.mapper

import com.benenfeldt.remote.dto.FriendStatusResponse
import com.benenfeldt.remote.dto.NotificationResponse
import com.teamhy2.friend.domain.model.FriendNotification
import com.teamhy2.friend.domain.model.FriendStatus

internal fun NotificationResponse.toDomain(): FriendNotification =
    FriendNotification(
        id = notificationId,
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        type = type,
        receivedAt = receivedAt,
    )

internal fun FriendStatus.toRemote(): FriendStatusResponse =
    when (this) {
        FriendStatus.PENDING -> FriendStatusResponse.PENDING
        FriendStatus.ACCEPTED -> FriendStatusResponse.ACCEPTED
        FriendStatus.REJECTED -> FriendStatusResponse.REJECTED
        FriendStatus.CANCELED -> FriendStatusResponse.CANCELED
        FriendStatus.NONE -> FriendStatusResponse.NONE
    }
