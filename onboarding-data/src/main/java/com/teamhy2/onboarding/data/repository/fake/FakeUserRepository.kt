package com.teamhy2.onboarding.data.repository.fake

import com.teamhy2.onboarding.domain.repository.NewUserRepository
import javax.inject.Inject

class FakeUserRepository
    @Inject
    constructor() : NewUserRepository {
        override suspend fun checkNicknameDuplication(nickname: String): Result<Boolean> {
            return Result.success(true)
        }

        override suspend fun signUp(
            nickname: String,
            department: String,
        ): Result<Unit> {
            return Result.success(Unit)
        }

        override suspend fun signIn(idToken: String): Result<Unit> {
            return Result.success(Unit)
        }
    }
