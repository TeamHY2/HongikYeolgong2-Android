package com.teamhy2.user.domain.repository

import com.teamhy2.user.domain.model.UserInfo

interface UserRepository {
    suspend fun getUserInfo(): Result<UserInfo>
}
