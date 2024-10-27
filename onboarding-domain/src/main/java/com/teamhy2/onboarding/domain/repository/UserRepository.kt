package com.teamhy2.onboarding.domain.repository

typealias AlreadyExist = Boolean
typealias Duplication = Boolean

interface UserRepository {
    suspend fun checkNicknameDuplication(nickname: String): Result<Duplication>

    suspend fun signUp(
        nickname: String,
        department: String,
    ): Result<Unit>

    suspend fun signIn(idToken: String): Result<AlreadyExist>

    suspend fun signOut(): Result<Unit>

    suspend fun withdraw(): Result<Unit>
}
