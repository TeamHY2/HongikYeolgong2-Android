package com.teamhy2.main.domain.repository

interface WebViewRepository {
    suspend fun fetchFirebaseUrls(): Map<String, String>
}
