package com.teamhy2.user.domain.model

data class UserInfo(
    val nickname: String,
    val email: String,
    val department: String,
) {
    companion object {
        val LOADING = UserInfo("불러오는 중..", "불러오는 중..", "불러오는 중..")
    }
}
