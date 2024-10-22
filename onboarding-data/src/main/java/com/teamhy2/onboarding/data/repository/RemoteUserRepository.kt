package com.teamhy2.onboarding.data.repository

import com.benenfeldt.remote.api.UserNoAuthService
import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignUpRequest
import com.benenfeldt.remote.mapper.toResult
import com.benenfeldt.remote.token.JwtManager
import com.teamhy2.onboarding.domain.repository.AlreadyExist
import com.teamhy2.onboarding.domain.repository.UserRepository
import javax.inject.Inject

class RemoteUserRepository
    @Inject
    constructor(
        private val userService: UserService,
        private val userNoAuthService: UserNoAuthService,
        private val jwtManager: JwtManager,
    ) : UserRepository {
        override suspend fun checkNicknameDuplication(nickname: String): Result<Boolean> {
            // TODO: 쿼리파라미터 수정완료 되면 주석 해제
//            return userNoAuthService.checkNicknameDuplication(nickname).toResult { it.data.duplicate }
            return Result.success(false)
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
            return userNoAuthService.signIn(
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

        override suspend fun withdraw(): Result<Unit> {
//        return userService.withdraw()
            // TODO: 추후 회원 탈퇴 서버 연결
            return Result.success(Unit)
        }
    }
