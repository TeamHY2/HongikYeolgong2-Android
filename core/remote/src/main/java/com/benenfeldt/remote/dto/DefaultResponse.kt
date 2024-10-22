package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse<T>(
    val code: Int,
    val `data`: T,
    val message: String,
    val status: String,
)
