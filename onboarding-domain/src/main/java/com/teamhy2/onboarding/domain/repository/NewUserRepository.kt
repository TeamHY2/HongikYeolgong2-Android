package com.teamhy2.onboarding.domain.repository

interface NewUserRepository {
    suspend fun checkNicknameDuplication(nickname: String): Result<Boolean>

    suspend fun signUp(
        nickname: String,
        department: String,
    ): Result<Unit>

    suspend fun signIn(idToken: String): Result<Unit>
}
