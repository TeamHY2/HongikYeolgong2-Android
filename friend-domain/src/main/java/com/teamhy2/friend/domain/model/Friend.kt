package com.teamhy2.friend.domain.model

import kotlin.time.Duration

data class Friend(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val studyTime: Duration,
)
