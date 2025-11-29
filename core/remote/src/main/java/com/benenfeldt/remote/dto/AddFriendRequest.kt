package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddFriendRequest(
    @SerialName("receiverId") val receiverId: Long,
)
