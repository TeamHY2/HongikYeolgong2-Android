package com.teamhy2.onboarding.data.repository

import com.benenfeldt.remote.api.UserPublicService
import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.benenfeldt.remote.mapper.toResult
import com.benenfeldt.remote.token.JwtManager
import com.teamhy2.core.auth.SocialSignIn
import com.teamhy2.onboarding.domain.repository.AlreadyExist
import com.teamhy2.onboarding.domain.repository.UserRepository
import javax.inject.Inject

class RemoteUserRepository
    @Inject
    constructor(
        private val userService: UserService,
        private val userPublicService: UserPublicService,
        private val jwtManager: JwtManager,
        private val socialSignIn: SocialSignIn,
    ) : UserRepository {
        override suspend fun checkNicknameDuplication(nickname: String): Result<Boolean> {
            return userPublicService.checkNicknameDuplication(nickname).toResult { it.data.duplicate }
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
            ).toResult { it.code in 200 until 300 }
        }

        override suspend fun signIn(idToken: String): Result<AlreadyExist> {
            return userPublicService.signIn(
                UserSignInRequest(
                    idToken = idToken,
                ),
            )
                .onSuccess {
                    jwtManager.saveAccessJwt(it.data.accessToken)
                }
                .toResult {
                    it.data.alreadyExist
                }
        }

        override suspend fun signOut(): Result<Unit> {
            return socialSignIn.requestSignOut()
                .onSuccess { jwtManager.clearAllTokens() }
        }

        override suspend fun withdraw(): Result<Unit> {
            return userService.withdraw()
                .onSuccess { jwtManager.clearAllTokens() }
                .toResult { }
        }
    }
