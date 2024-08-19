package com.teamhy2.main.domain

interface FirebaseRepository {
    suspend fun fetchFirebaseUrls(): Map<String, String>
}
