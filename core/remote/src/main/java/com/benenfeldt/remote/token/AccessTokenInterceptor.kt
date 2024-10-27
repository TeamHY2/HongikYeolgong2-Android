package com.benenfeldt.remote.token

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor
    @Inject
    constructor(
        private val jwtManager: JwtManager,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token: String =
                runBlocking {
                    jwtManager.getAccessJwt() ?: ""
                }

            if (token.isEmpty()) {
                return chain.proceed(chain.request())
            }

            return chain.proceed(
                chain
                    .request()
                    .newBuilder()
                    .addHeader(HEADER_AUTHORIZATION_NAME, if (token.startsWith(prefix = TOKEN_TYPE)) token else "$TOKEN_TYPE$token")
                    .build(),
            )
        }

        companion object {
            private const val HEADER_AUTHORIZATION_NAME = "Authorization"
            private const val TOKEN_TYPE = "Bearer "
        }
    }
