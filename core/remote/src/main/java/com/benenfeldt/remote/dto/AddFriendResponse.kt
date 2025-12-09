package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddFriendResponse(
    @SerialName("id") val friendId: Long,
    @SerialName("receiverId") val receiverId: Long,
    @SerialName("friendStatus") val friendStatus: FriendStatusResponse,
)
