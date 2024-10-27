package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserNicknameDuplicationResponse(
    val duplicate: Boolean,
    val nickname: String,
)
