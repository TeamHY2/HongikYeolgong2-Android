package com.teamhy2.onboarding.data.repository

import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.onboarding.domain.repository.UserRepository
import javax.inject.Inject

class RemoteUserRepository
    @Inject
    constructor(
        private val userService: UserService,
    ) : UserRepository {
        override suspend fun checkNicknameDuplication(nickname: String): Result<Boolean> {
            return userService.checkNicknameDuplication(nickname).toResult { it.data.duplicate }
        }

        override suspend fun signUp(
            nickname: String,
            department: String,
        ): Result<Unit> {
            return userService.signUp(
                UserSignUpRequest(
                    nickname = nickname,
                    department = department,
                ),
            )
        }

        override suspend fun signIn(idToken: String): Result<Unit> {
            return runCatching {
                userService.signIn(
                    UserSignInRequest(
                        idToken = idToken,
                    ),
                )
            }
        }

        override suspend fun withdraw(): Result<Unit> {
//        return userService.withdraw()
            return Result.success(Unit)
        }
    }
