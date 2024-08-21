package com.teamhy2.main.domain

import com.teamhy2.main.model.WiseSaying

interface HomeContentRepository {
    suspend fun fetchWiseSaying(): WiseSaying
}
