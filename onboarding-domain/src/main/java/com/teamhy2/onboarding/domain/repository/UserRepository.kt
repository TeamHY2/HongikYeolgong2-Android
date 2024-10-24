package com.teamhy2.onboarding.domain.repository

interface UserRepository {
    suspend fun checkUserExists(uid: String): Boolean

    suspend fun checkNicknameDuplication(nickname: String): Boolean

    suspend fun signUp(
        nickname: String,
        department: String,
    )

    suspend fun withdraw()
}
