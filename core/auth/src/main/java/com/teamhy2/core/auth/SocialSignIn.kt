package com.teamhy2.core.auth

typealias IdToken = String

sealed interface SocialSignIn {
    suspend fun requestSignInWithIdToken(): Result<IdToken>
}
