package com.teamhy2.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import com.teamhy2.designsystem.ui.theme.HY2Theme

@Composable
fun OnboardingRoute(
    modifier: Modifier = Modifier,
    onGoogleLoginClick: () -> Unit,
) {
    OnboardingScreen(onGoogleLoginClick = onGoogleLoginClick)
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onGoogleLoginClick: () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        AndroidView(
            factory = {
                val signInButton =
                    SignInButton(it).apply {
                        setOnClickListener {
                            onGoogleLoginClick()
                        }
                    }
                signInButton
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    HY2Theme {
        OnboardingScreen(
            modifier = Modifier.fillMaxSize(),
            onGoogleLoginClick = { },
        )
    }
}
