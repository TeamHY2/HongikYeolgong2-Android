package com.teamhy2.main.domain

interface WebViewRepository {
    suspend fun fetchFirebaseUrls(): Map<String, String>
}
