package com.teamhy2.main.domain

interface FirebaseRepository {
    suspend fun fetchWebViewUrls(): Map<String, String>
}
