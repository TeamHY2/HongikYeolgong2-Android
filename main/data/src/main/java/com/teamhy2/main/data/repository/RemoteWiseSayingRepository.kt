package com.teamhy2.main.data.repository

import com.benenfeldt.remote.api.WiseSayingService
import com.benenfeldt.remote.mapper.toResult
import com.teamhy2.main.data.mapper.toDomain
import com.teamhy2.main.domain.model.WiseSaying
import com.teamhy2.main.domain.repository.WiseSayingRepository
import javax.inject.Inject

class RemoteWiseSayingRepository @Inject constructor(
    private val wiseSayingService: WiseSayingService,
) : WiseSayingRepository {
    override suspend fun fetchWiseSaying(): Result<WiseSaying> {
        return wiseSayingService.getWiseSaying().toResult { baseResponse ->
            baseResponse.data.toDomain()
        }
    }
}
