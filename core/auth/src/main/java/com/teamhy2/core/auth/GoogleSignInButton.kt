package com.teamhy2.core.auth

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White

@Composable
fun GoogleSignInButton(
    onGoogleSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onGoogleSignInClick,
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
            onGoogleSignInClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
