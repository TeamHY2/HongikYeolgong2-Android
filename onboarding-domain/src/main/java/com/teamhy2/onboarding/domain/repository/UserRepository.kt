package com.teamhy2.onboarding.domain.repository

typealias AlreadyExist = Boolean

interface UserRepository {
    suspend fun checkNicknameDuplication(nickname: String): Result<Boolean>

    suspend fun signUp(
        nickname: String,
        department: String,
    ): Result<Unit>

    suspend fun signIn(idToken: String): Result<AlreadyExist>

    suspend fun withdraw(): Result<Unit>
}
