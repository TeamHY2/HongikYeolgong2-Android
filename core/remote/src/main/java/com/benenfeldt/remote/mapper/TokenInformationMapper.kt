package com.benenfeldt.remote.mapper

import com.benenfeldt.remote.dto.TokenInformationResponse
import com.benenfeldt.remote.token.TokenInformation
import com.benenfeldt.remote.token.TokenRole

fun TokenInformationResponse.toTokenInformation(): TokenInformation {
    return TokenInformation(
        role = TokenRole.valueOf(role),
        isValidToken = validToken,
    )
}
