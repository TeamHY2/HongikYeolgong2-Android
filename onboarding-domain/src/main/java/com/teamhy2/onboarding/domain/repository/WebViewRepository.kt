package com.teamhy2.onboarding.domain.repository

interface WebViewRepository {
    suspend fun fetchFirebaseUrls(): Map<String, String>
}
