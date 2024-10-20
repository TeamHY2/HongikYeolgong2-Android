package com.teamhy2.core.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
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
                .setNonce(createHashedNonce())
                .build()
        private val request: GetCredentialRequest =
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
                val result =
                    credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                googleIdTokenCredential.idToken
            }
        }

        companion object {
            private const val DIGEST_ALGORITHM = "SHA-256"
        }
    }
