package com.teamhy2.user.data.mapper

import com.benenfeldt.remote.dto.UserInfoResponse
import com.teamhy2.user.domain.model.UserInfo

fun UserInfoResponse.toDomain(): UserInfo {
    return UserInfo(
        nickname = nickname,
        email = email,
        department = department,
    )
}
