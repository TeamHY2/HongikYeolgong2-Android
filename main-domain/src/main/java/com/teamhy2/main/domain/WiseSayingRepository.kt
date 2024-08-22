package com.teamhy2.main.domain

import com.teamhy2.main.model.WiseSaying

interface WiseSayingRepository {
    suspend fun fetchWiseSaying(): WiseSaying
}
