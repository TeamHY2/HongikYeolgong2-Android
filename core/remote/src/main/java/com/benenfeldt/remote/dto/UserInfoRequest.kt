package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    val nickname: String,
    val department: String,
)
