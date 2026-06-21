package com.teamhy2.feature.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.HY2Button
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun NeedUpdateScreen(
    onExitClick: () -> Unit,
    onUpdateClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(BackgroundBlack),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .background(Gray800, RoundedCornerShape(8.dp))
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.img_error),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.need_update_description),
                style = HY2Typography().body06.copy(fontWeight = FontWeight(600)),
                color = Gray100,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                HY2Button(
                    text = stringResource(id = R.string.need_update_quit),
                    onClick = onExitClick,
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(46.dp),
                    backgroundColor = Gray600,
                    textColor = Gray200,
                )
                Spacer(modifier = Modifier.width(12.dp))
                HY2Button(
                    text = stringResource(id = R.string.need_update_go_update),
                    onClick = onUpdateClick,
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(46.dp),
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview
@Composable
private fun NeedUpdateScreenPreview() {
    HY2Theme {
        NeedUpdateScreen(onExitClick = {}, onUpdateClick = {})
    }
}
