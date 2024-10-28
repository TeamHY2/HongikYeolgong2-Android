package com.benenfeldt.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val id: Int,
    val department: String,
    val nickname: String,
    @SerialName("username")
    val email: String,
)
