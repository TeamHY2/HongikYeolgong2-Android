package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse<T>(
    val code: Int,
    val `data`: T,
    val status: String,
    val message: String,
)
