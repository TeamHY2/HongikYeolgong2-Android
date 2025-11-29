package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendSearchResponse(
    @SerialName("userId") val userId: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("friendStatus") val friendStatus: FriendStatusResponse,
)

@Serializable
enum class FriendStatusResponse {
    @SerialName("PENDING")
    PENDING,

    @SerialName("ACCEPTED")
    ACCEPTED,

    @SerialName("REJECTED")
    REJECTED,

    @SerialName("NONE")
    NONE,
}
