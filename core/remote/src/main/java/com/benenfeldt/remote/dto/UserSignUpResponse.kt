package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpResponse(
    val id: Int,
    val department: String,
    val nickname: String,
    val username: String,
)
