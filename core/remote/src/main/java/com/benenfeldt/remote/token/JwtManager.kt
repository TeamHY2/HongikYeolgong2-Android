package com.benenfeldt.remote.token

interface JwtManager {
    suspend fun saveAccessJwt(token: String)

    suspend fun getAccessJwt(): String?

    suspend fun saveGoogleIdToken(token: String)

    suspend fun getGoogleIdToken(): String?

    suspend fun clearAllTokens()
}
