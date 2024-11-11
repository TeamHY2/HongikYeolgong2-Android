package com.benenfeldt.remote.token

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class DefaultAuthenticator
    @Inject
    constructor(
        private val authCallback: AuthCallback,
        private val jwtManager: JwtManager,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (response.code == 401) {
                runBlocking { jwtManager.clearAllTokens() }
                authCallback.onAuthRequired()

                // null을 반환하여 현재 요청을 중단
                return null
            }
            return null
        }
    }
