package com.teamhy2.record.components

import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.hongikyeolgong2.record.presentation.R
import com.teamhy2.record.RecordContent
import com.teamhy2.record.RecordState
import com.teamhy2.record.domain.model.StudyDuration

@Composable
fun ShareImageComponent(
    recordState: RecordState,
    onCaptured: (Bitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    val localView: View = LocalView.current
    var componentPosition: Offset by remember { mutableStateOf(Offset.Zero) }
    var componentSize: IntSize by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .paint(
                    painter = painterResource(id = R.drawable.img_shrae_image_background),
                    contentScale = ContentScale.FillBounds,
                )
                .onGloballyPositioned { coordinates ->
                    componentPosition = coordinates.positionInRoot()
                    componentSize = coordinates.size
                },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RecordContent(
            recordState = recordState,
            onPreviousMonthClick = { },
            onNextMonthClick = { },
            onDayClicked = { },
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 42.dp),
        )
        Spacer(modifier = Modifier.height(18.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_share_image_star),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(32.dp))
    }

    LaunchedEffect(componentSize) {
        val rootViewBitmap: Bitmap = localView.drawToBitmap()
        if (componentSize.width > 0 && componentSize.height > 0) {
            val croppedBitmap: Bitmap =
                Bitmap.createBitmap(
                    rootViewBitmap,
                    componentPosition.x.toInt(),
                    componentPosition.y.toInt(),
                    componentSize.width,
                    componentSize.height,
                )
            onCaptured(croppedBitmap)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShareImageComponentPreview() {
    val sampleStudySummary =
        StudyDuration(
            yearHours = 200,
            yearMinutes = 4,
            monthHours = 50,
            monthMinutes = 30,
            dayHours = 3,
            dayMinutes = 24,
        )

    val sampleCalendar = Calendar(studyDays = emptyList())

    val sampleRecordState =
        RecordState(
            date = "February 22, 2025",
            studyDuration = sampleStudySummary,
            calendar = sampleCalendar,
        )

    ShareImageComponent(
        recordState = sampleRecordState,
        onCaptured = { },
    )
}
