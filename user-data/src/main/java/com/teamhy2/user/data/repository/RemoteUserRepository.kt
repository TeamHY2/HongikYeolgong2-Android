package com.teamhy2.user.data.repository

import com.benenfeldt.remote.api.UserPublicService
import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.dto.UserInfoRequest
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.benenfeldt.remote.mapper.toResult
import com.benenfeldt.remote.token.JwtManager
import com.teamhy2.user.data.mapper.toDomain
import com.teamhy2.user.domain.model.UserInfo
import com.teamhy2.user.domain.repository.AlreadyExist
import com.teamhy2.user.domain.repository.Duplication
import com.teamhy2.user.domain.repository.UserRepository
import javax.inject.Inject

class RemoteUserRepository
    @Inject
    constructor(
        private val userService: UserService,
        private val userPublicService: UserPublicService,
        private val jwtManager: JwtManager,
    ) : UserRepository {
        override suspend fun checkNicknameDuplication(nickname: String): Result<Duplication> {
            return userPublicService.checkNicknameDuplication(nickname).toResult { baseResponse ->
                baseResponse.data.duplicate
            }
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
                .onSuccess { baseResponse ->
                    if (baseResponse.isSuccess()) {
                        jwtManager.clearAllTokens()
                        jwtManager.saveAccessJwt(baseResponse.data.accessToken)
                    }
                }
                .toResult()
        }

        override suspend fun signIn(idToken: String): Result<AlreadyExist> {
            return userPublicService.signIn(
                UserSignInRequest(
                    idToken = idToken,
                ),
            )
                .onSuccess { baseResponse ->
                    if (baseResponse.isSuccess()) {
                        jwtManager.saveAccessJwt(baseResponse.data.accessToken)
                    }
                }
                .toResult { baseResponse ->
                    baseResponse.data.alreadyExist
                }
        }

        override suspend fun signOut(): Result<Unit> {
            return runCatching {
                jwtManager.clearAllTokens()
            }
        }

        override suspend fun withdraw(): Result<Unit> {
            return userService.withdraw()
                .onSuccess { baseResponse ->
                    if (baseResponse.isSuccess()) {
                        jwtManager.clearAllTokens()
                    }
                }
                .toResult()
        }

        override suspend fun getUserInfo(): Result<UserInfo> {
            return userService.getUserInfo()
                .toResult { it.data.toDomain() }
        }

        override suspend fun modifyUserInfo(
            nickname: String,
            department: String,
        ): Result<Unit> {
            return userService.modifyUserInfo(
                UserInfoRequest(
                    nickname = nickname,
                    department = department,
                ),
            ).toResult()
        }
    }
