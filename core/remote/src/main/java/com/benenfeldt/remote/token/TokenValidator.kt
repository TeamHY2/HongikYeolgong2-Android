package com.benenfeldt.remote.token

import com.benenfeldt.remote.api.TokenService
import com.benenfeldt.remote.mapper.toResult
import com.benenfeldt.remote.mapper.toTokenInformation
import javax.inject.Inject

class TokenValidator @Inject constructor(
    private val tokenService: TokenService,
) {
    suspend fun validate(): Result<TokenInformation> {
        return tokenService.getTokenInformation()
            .toResult { it.data.toTokenInformation() }
    }
}
