package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenInformationResponse(
    val role: String,
    val validToken: Boolean,
)
