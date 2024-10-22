package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.DefaultResponse
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignInResponse
import com.benenfeldt.remote.dto.UserSignUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("/api/v1/user/duplicate-nickname")
    suspend fun checkNicknameDuplication(
        @Query("nickname") nickname: String,
    ): Result<Boolean>

    @POST("/api/v1/auth/login-google")
    suspend fun signIn(
        @Body userSignInRequest: UserSignInRequest,
    ): Result<DefaultResponse<UserSignInResponse>>

    @POST("/api/v1/user/join")
    suspend fun signUp(
        @Body signUpRequest: UserSignUpRequest,
    ): Result<Unit>
}
