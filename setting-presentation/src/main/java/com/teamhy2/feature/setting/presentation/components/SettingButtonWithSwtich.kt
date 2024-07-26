package com.teamhy2.feature.setting.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.HY2Switch
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme

private const val HEIGHT = 52
private const val CORNER_RADIUS = 8
private const val PADDING_START = 16
private const val PADDING_END = 14
private const val PADDING_VERTICAL = 0

@Composable
fun SettingButtonWithSwitch(
    text: String,
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CORNER_RADIUS.dp))
                .height(HEIGHT.dp),
        shape = RoundedCornerShape(CORNER_RADIUS.dp),
        color = Gray800,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PADDING_START.dp,
                        end = PADDING_END.dp,
                        top = PADDING_VERTICAL.dp,
                        bottom = PADDING_VERTICAL.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                color = Gray200,
                style = HY2Theme.typography.body05,
            )
            HY2Switch(isChecked = isChecked, onCheckedChanged = onCheckedChanged)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingButtonWithSwitchPreview() {
    var isChecked by remember { mutableStateOf(false) }

    HY2Theme {
        SettingButtonWithSwitch(
            text = "열람실 종료 시간 알림",
            isChecked = isChecked,
            onCheckedChanged = { isChecked = it },
        )
    }
}
