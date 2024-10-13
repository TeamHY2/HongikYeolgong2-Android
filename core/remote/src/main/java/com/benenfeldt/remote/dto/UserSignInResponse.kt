package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSignInResponse(
    val accessToken: String,
    val alreadyExist: Boolean,
)
