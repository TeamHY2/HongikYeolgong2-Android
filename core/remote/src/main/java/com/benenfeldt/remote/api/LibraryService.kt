package com.benenfeldt.remote.api

import com.benenfeldt.remote.dto.BaseResponse
import com.benenfeldt.remote.dto.StudyRoomHourDurationResponse
import retrofit2.http.GET

interface LibraryService {
    @GET("/api/v1/library")
    suspend fun getStudyRoomHourDuration(): Result<BaseResponse<StudyRoomHourDurationResponse>>
}
