package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.DefaultResponse
import com.benenfeldt.remote.dto.UserNicknameDuplicationResponse
import com.benenfeldt.remote.dto.UserSignInRequest
import com.benenfeldt.remote.dto.UserSignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserNoAuthService {
    @GET("/api/v1/user/duplicate-nickname")
    suspend fun checkNicknameDuplication(
        @Query("nickname") nickname: String,
    ): Result<DefaultResponse<UserNicknameDuplicationResponse>>

    @POST("/api/v1/auth/login-google")
    suspend fun signIn(
        @Body userSignInRequest: UserSignInRequest,
    ): Result<DefaultResponse<UserSignInResponse>>
}
