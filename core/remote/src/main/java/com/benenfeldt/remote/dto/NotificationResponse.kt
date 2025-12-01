package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val notificationId: Long,
    val type: String,
    val content: String,
    val receivedAt: String,
    val friendId: Long?,
    val receiverId: Long,
    val senderId: Long?,
    val senderNickname: String?,
)
