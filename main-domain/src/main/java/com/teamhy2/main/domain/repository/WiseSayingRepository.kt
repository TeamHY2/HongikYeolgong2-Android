package com.teamhy2.main.domain.repository

import com.teamhy2.main.domain.model.WiseSaying

interface WiseSayingRepository {
    suspend fun fetchWiseSaying(): WiseSaying
}
