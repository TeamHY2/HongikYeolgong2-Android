package com.teamhy2.core.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun GoogleSignInButton(
    onGoogleSignInDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()

        val request: GetCredentialRequest =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

        coroutineScope.launch {
            runCatching {
                val result =
                    credentialManager.getCredential(
                        request = request,
                        context = context,
                    )
                val credential = result.credential

                val googleIdTokenCredential =
                    GoogleIdTokenCredential
                        .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                Log.i("bandal", googleIdToken)
            }
                .onSuccess {
                    Toast.makeText(context, "You are signed in!", Toast.LENGTH_SHORT).show()
                    onGoogleSignInDone()
                }.onFailure {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors().copy(containerColor = White),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Image(painter = painterResource(id = R.drawable.logo_google), contentDescription = null, modifier = Modifier.size(24.dp))
        }
        Text(
            text = stringResource(R.string.google_sign_in_text),
            style = HY2Typography().body02,
            color = Gray800,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun GoogleSignInButtonPreview() {
    HY2Theme {
        GoogleSignInButton(
            onGoogleSignInDone = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
