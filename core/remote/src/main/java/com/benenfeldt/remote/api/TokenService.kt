package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.TokenInformationResponse
import retrofit2.http.GET

interface TokenService {
    @GET("/api/v1/token")
    suspend fun getTokenInformation(): Result<BaseResponse<TokenInformationResponse>>
}
