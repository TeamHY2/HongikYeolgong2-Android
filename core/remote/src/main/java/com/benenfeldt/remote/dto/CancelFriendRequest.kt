package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CancelFriendRequest(
    @SerialName("cancelUserId") val cancelUserId: Long,
)
