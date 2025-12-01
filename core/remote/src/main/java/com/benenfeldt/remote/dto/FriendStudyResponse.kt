package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FriendStudyResponse(
    val userId: Long,
    val friendId: Long,
    val friendNickname: String,
    val studyTime: String,
)
