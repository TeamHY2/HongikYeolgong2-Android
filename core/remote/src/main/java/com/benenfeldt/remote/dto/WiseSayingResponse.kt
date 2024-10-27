package com.benenfeldt.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WiseSayingResponse(
    val id: Int,
    val author: String,
    val quote: String,
)
