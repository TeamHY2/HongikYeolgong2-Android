package com.teamhy2.onboarding.data.repository

import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.teamhy2.onboarding.domain.repository.NewUserRepository
import javax.inject.Inject

class RemoteUserRepository
    @Inject
    constructor(
        private val userService: UserService,
    ) : NewUserRepository {
        override suspend fun checkNicknameDuplication(nickname: String): Result<Boolean> {
            return userService.checkNicknameDuplication(nickname)
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
    }
