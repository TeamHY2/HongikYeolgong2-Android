package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeviceTokenResponse(
    val deviceToken: String,
    val id: Int,
    val nickname: String,
    val username: String,
)
