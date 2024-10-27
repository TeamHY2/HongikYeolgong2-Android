package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequest(
    val nickname: String,
    val department: String,
)
