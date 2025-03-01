package com.teamhy2.record.components

import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hongikyeolgong2.calendar.model.Calendar
import com.teamhy2.designsystem.common.HY2IconTextButton
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.record.presentation.R
import com.teamhy2.record.RecordState
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.share.RecordShareImageManager

@Composable
fun RecordShareFullScreenDialog(
    recordState: RecordState,
    onDismiss: () -> Unit,
    onSaveImageComplete: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .paint(
                        painter = painterResource(id = R.drawable.img_record_share_background),
                        contentScale = ContentScale.Crop,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = onDismiss, modifier = Modifier.padding(end = 20.dp)) {
                    Icon(
                        painter = painterResource(id = com.teamhy2.designsystem.R.drawable.ic_close),
                        contentDescription = "닫기",
                        tint = Color.White,
                    )
                }
            }
            Text(
                text = "열공 기록을 공유해보세요!",
                color = Gray100,
                style = HY2Typography().title03,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            ShareImageComponent(
                recordState = recordState,
                onCaptured = { bitmap ->
                    capturedBitmap = bitmap
                },
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp)
                        .border(
                            width = 1.dp,
                            color = Gray800,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .clip(RoundedCornerShape(4.dp)),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, top = 20.dp, bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                HY2IconTextButton(
                    text = "이미지 저장",
                    iconResId = R.drawable.ic_save_image,
                    backgroundColor = Gray800,
                    textColor = Gray100,
                    onClick = {
                        capturedBitmap?.let { bitmap ->
                            RecordShareImageManager.imageSaver.saveImage(
                                context,
                                bitmap,
                            ) { success ->
                                onSaveImageComplete(success)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                )

                HY2IconTextButton(
                    text = "인스타 공유",
                    iconResId = R.drawable.ic_share,
                    backgroundColor = Gray800,
                    onClick = {
                        capturedBitmap?.let { bitmap ->
                            RecordShareImageManager.imageSharer.shareImage(context, bitmap)
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordShareFullScreenDialogPreview() {
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

    HY2Theme {
        RecordShareFullScreenDialog(
            recordState = sampleRecordState,
            onDismiss = {},
            onSaveImageComplete = {},
        )
    }
}
