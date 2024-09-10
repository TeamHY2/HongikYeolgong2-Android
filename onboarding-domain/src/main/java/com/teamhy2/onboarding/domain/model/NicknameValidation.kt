package com.teamhy2.onboarding.domain.model

object NicknameValidation {
    fun validate(nickname: String): Boolean {
        val regex = "^[가-힣a-zA-Z0-9]{2,8}$".toRegex()
        return regex.matches(nickname)
    }
}
