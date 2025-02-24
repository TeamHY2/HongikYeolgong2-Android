package com.teamhy2.record.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.record.presentation.R

@Composable
internal fun DatePanel(
    date: String,
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = date,
            style = HY2Typography().body04,
            color = Gray300,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.record_time_format, hour, minute),
            style =
                HY2Typography().body01.copy(
                    brush =
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFD3D6E0),
                                Color(0x99D3D6E0),
                            ),
                        ),
                ),
            color = Gray100,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DatePanelPreview() {
    HY2Theme {
        DatePanel(
            date = "February 22, 2025",
            hour = 3,
            minute = 24,
        )
    }
}
