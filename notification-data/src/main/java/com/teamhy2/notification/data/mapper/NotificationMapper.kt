package com.teamhy2.notification.data.mapper

import com.benenfeldt.remote.dto.NotificationResponse
import com.teamhy2.notification.domain.model.Notification
import com.teamhy2.notification.domain.model.NotificationType

fun NotificationResponse.toDomain(): Notification {
    return Notification(
        notificationId = notificationId,
        type = type.toNotificationType(),
        content = content,
        receivedAt = receivedAt,
        friendId = friendId,
        receiverId = receiverId,
        senderId = senderId,
        senderNickname = senderNickname,
    )
}

private fun String.toNotificationType(): NotificationType {
    return when (uppercase()) {
        "REQUEST" -> NotificationType.REQUEST
        else -> NotificationType.REQUEST
    }
}
