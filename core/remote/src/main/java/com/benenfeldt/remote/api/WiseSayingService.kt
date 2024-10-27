package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.WiseSayingResponse
import retrofit2.http.GET

interface WiseSayingService {
    @GET("/api/v1/wise-saying")
    fun getWiseSaying(): Result<BaseResponse<WiseSayingResponse>>
}
