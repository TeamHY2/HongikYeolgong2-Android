package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFriendStatusResponse(
    @SerialName("senderId") val senderId: Long,
    @SerialName("friendStatus") val friendStatus: FriendStatusResponse,
)
