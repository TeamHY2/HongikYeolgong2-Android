package com.teamhy2.notification.domain.model

data class Notification(
    val notificationId: Long,
    val type: NotificationType,
    val content: String,
    val receivedAt: String,
    val friendId: Long?,
    val receiverId: Long,
    val senderId: Long?,
    val senderNickname: String?,
)

enum class NotificationType {
    REQUEST,
    ACCEPT,
    REJECT,
    CANCEL,
}
