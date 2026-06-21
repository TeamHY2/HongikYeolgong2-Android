package com.teamhy2.core.auth

import android.content.Context
import android.util.Base64
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.security.MessageDigest
import java.security.SecureRandom

private typealias IdToken = String

class GoogleSignIn(private val context: Context) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun requestSignInWithIdToken(): Result<IdToken> =
        runCatching {
            val response = getCredentialWithFallback()
            extractIdToken(response)
        }

    private suspend fun getCredentialWithFallback(): GetCredentialResponse {
        return try {
            credentialManager.getCredential(
                request = buildAuthorizedAccountsRequest(),
                context = context,
            )
        } catch (e: NoCredentialException) {
            credentialManager.getCredential(
                request = buildSignInWithGoogleRequest(),
                context = context,
            )
        }
    }

    private fun buildAuthorizedAccountsRequest(): GetCredentialRequest {
        val option =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setAutoSelectEnabled(true)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setNonce(generateHashedNonce())
                .build()
        return GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
    }

    private fun buildSignInWithGoogleRequest(): GetCredentialRequest {
        val option =
            GetSignInWithGoogleOption.Builder(serverClientId = BuildConfig.WEB_CLIENT_ID)
                .setNonce(generateHashedNonce())
                .build()
        return GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
    }

    private fun extractIdToken(response: GetCredentialResponse): IdToken {
        val credential = response.credential
        check(
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL,
        ) { "Unexpected credential type: ${credential::class.java.name}" }
        return try {
            GoogleIdTokenCredential.createFrom(credential.data).idToken
        } catch (e: GoogleIdTokenParsingException) {
            throw IllegalStateException("Failed to parse Google ID token", e)
        }
    }

    suspend fun requestSignOut(): Result<Unit> =
        runCatching {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }

    private fun generateHashedNonce(): String {
        val rawBytes = ByteArray(NONCE_BYTE_LENGTH).also { SecureRandom().nextBytes(it) }
        val rawNonce = Base64.encodeToString(rawBytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        val digest = MessageDigest.getInstance(DIGEST_ALGORITHM).digest(rawNonce.toByteArray())
        return digest.joinToString(separator = "") { "%02x".format(it) }
    }

    companion object {
        private const val DIGEST_ALGORITHM = "SHA-256"
        private const val NONCE_BYTE_LENGTH = 32
    }
}
