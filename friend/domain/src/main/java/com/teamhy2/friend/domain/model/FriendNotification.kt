package com.teamhy2.friend.domain.model

data class FriendNotification(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val type: String,
    val receivedAt: String,
)
