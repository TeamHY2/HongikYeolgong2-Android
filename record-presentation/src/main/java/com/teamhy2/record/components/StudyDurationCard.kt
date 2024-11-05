package com.teamhy2.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Typography

@Composable
fun StudySummaryCard(
    title: String,
    studyHours: Int,
    studyMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Gray800, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Gray600, shape = RoundedCornerShape(8.dp))
                .padding(start = 28.dp, top = 18.dp, bottom = 18.dp),
    ) {
        Text(
            text = title,
            style = HY2Typography().body05,
            color = Gray200,
            modifier = Modifier.height(26.dp),
        )
        Text(
            text = "${studyHours}H ${studyMinutes}M",
            style = HY2Typography().title03,
            color = Gray100,
            modifier = Modifier.height(26.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StudySummaryCardPreview() {
    StudySummaryCard(
        title = "연간",
        studyHours = 200,
        studyMinutes = 4,
    )
}

@Preview(showBackground = true)
@Composable
fun StudySummaryCardGroupPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Row {
            StudySummaryCard(
                title = "연간",
                studyHours = 200,
                studyMinutes = 4,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            StudySummaryCard(
                title = "이번학기",
                studyHours = 120,
                studyMinutes = 4,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row {
            StudySummaryCard(
                title = "월간",
                studyHours = 50,
                studyMinutes = 4,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            StudySummaryCard(
                title = "투데이",
                studyHours = 3,
                studyMinutes = 24,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
