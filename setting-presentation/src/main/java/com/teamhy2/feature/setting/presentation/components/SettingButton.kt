package com.teamhy2.feature.setting.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.hongikyeolgong2.setting.presentation.R

private const val CORNER_RADIUS = 8
private const val BUTTON_HEIGHT = 52
private const val PADDING_START = 16
private const val PADDING_END = 11
private const val ARROW_SIZE = 24

@Composable
fun SettingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT.dp)
                .clip(RoundedCornerShape(CORNER_RADIUS.dp))
                .clickable(onClick = onClick),
        color = Gray800,
        shape = RoundedCornerShape(CORNER_RADIUS.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(start = PADDING_START.dp, end = PADDING_END.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = text,
                color = Gray200,
                style = HY2Theme.typography.body05,
            )
            Image(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(ARROW_SIZE.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingItemPreview() {
    HY2Theme {
        SettingButton(text = "공지사항", onClick = { })
    }
}
