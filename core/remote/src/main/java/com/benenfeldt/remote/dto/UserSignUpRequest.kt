package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpRequest(
    val department: String,
    val nickname: String,
)
