package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeviceTokenRequest(
    val deviceToken: String,
)
