package com.teamhy2.core.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class GoogleSignIn
    @Inject
    constructor(
        private val context: Context,
    ) : SocialSignIn {
        private val credentialManager = CredentialManager.create(context)
        private val googleIdOption: GetGoogleIdOption =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .setNonce(createHashedNonce())
                .build()
        private val credentialRequest: GetCredentialRequest =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

        private fun createHashedNonce(): String {
            val rawNonce: String = UUID.randomUUID().toString()
            val bytes: ByteArray = rawNonce.toByteArray()
            val md: MessageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM)
            val digest: ByteArray = md.digest(bytes)
            val hashedNonce: String =
                digest.fold("") { accumulator, it ->
                    accumulator + "%02x".format(it)
                }
            return hashedNonce
        }

        override suspend fun requestSignInWithIdToken(): Result<IdToken> {
            return runCatching {
                val credentialResponse: GetCredentialResponse =
                    credentialManager.getCredential(
                        request = credentialRequest,
                        context = context,
                    )

                val credential = credentialResponse.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                googleIdTokenCredential.idToken
            }
        }

        override suspend fun requestSignOut(): Result<Unit> {
            return runCatching {
                credentialManager.clearCredentialState(request = ClearCredentialStateRequest())
            }
        }

        companion object {
            private const val DIGEST_ALGORITHM = "SHA-256"
        }
    }
