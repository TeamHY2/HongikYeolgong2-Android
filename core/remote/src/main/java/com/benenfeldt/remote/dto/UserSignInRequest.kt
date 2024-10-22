package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserSignInRequest(
    val idToken: String,
)
