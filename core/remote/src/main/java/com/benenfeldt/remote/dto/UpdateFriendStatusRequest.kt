package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFriendStatusRequest(
    @SerialName("notificationId") val notificationId: Long,
    @SerialName("friendId") val friendId: Long,
    @SerialName("senderId") val senderId: Long,
    @SerialName("friendStatus") val friendStatus: FriendStatusResponse,
)
