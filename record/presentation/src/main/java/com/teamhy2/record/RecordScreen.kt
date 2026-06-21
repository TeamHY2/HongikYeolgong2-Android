package com.teamhy2.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.model.StudyDay
import com.hongikyeolgong2.calendar.presentation.Hy2Calendar
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.common.HY2IconTextButton
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalShowToast
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.hongikyeolgong2.record.presentation.R
import com.teamhy2.record.components.DatePanel
import com.teamhy2.record.components.RecordShareFullScreenDialog
import com.teamhy2.record.components.StudyDurationCard
import com.teamhy2.record.components.StudyDurationCardType
import com.teamhy2.record.domain.model.StudyDuration
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RecordRoute(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    val recordState by recordViewModel.collectAsState()

    val localShowSnackBar = LocalShowSnackBar.current
    val localToast = LocalShowToast.current
    val tracker = LocalTracker.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var isRecordShareDialogShow by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tracker.trackEvent("Record")
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            recordViewModel.fetchStudyDuration()
            recordViewModel.fetchCalendar()
        }
    }

    recordViewModel.collectSideEffect {
        when (it) {
            is RecordSideEffect.ShowError -> {
                localShowSnackBar.showSnackBar(it.throwable.message)
            }

            is RecordSideEffect.ShowSnackBar -> {
                localShowSnackBar.showSnackBar(it.message)
            }

            is RecordSideEffect.ShowToast -> {
                localToast.showToast(it.message)
            }
        }
    }

    if (recordState.isLoading) {
        HY2CircularLoading()
    } else {
        RecordScreen(
            recordState = recordState,
            onPreviousMonthClick = { recordViewModel.updateCalendarMonth(false) },
            onNextMonthClick = { recordViewModel.updateCalendarMonth(true) },
            onDayClicked = { recordViewModel.updateSelectedStudyDay(it) },
            onRecordShareButtonClick = { isRecordShareDialogShow = true },
            modifier = modifier,
        )
        if (isRecordShareDialogShow) {
            RecordShareFullScreenDialog(
                recordState = recordState,
                onDismiss = {
                    isRecordShareDialogShow = false
                },
                onSaveImageComplete = { isSuccess ->
                    recordViewModel.handleImageSaveResult(isSuccess)
                    isRecordShareDialogShow =
                        when (isSuccess) {
                            true -> false
                            false -> true
                        }
                },
            )
        }
    }
}

@Composable
fun RecordScreen(
    recordState: RecordState,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDayClicked: (StudyDay?) -> Unit,
    onRecordShareButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        RecordContent(
            recordState = recordState,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            onDayClicked = onDayClicked,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, top = 32.dp),
        )
        HY2IconTextButton(
            text = "기록 공유하기",
            iconResId = R.drawable.ic_share,
            backgroundColor = Gray800,
            textColor = Gray100,
            onClick = onRecordShareButtonClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 36.dp),
        )
    }
}

@Composable
fun RecordContent(
    recordState: RecordState,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDayClicked: (StudyDay?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        if (recordState.selectedStudyDay == null) {
            DatePanel(
                date = recordState.date,
                hour = recordState.studyDuration.dayHours,
                minute = recordState.studyDuration.dayMinutes,
            )
        } else {
            DatePanel(
                date = recordState.selectedStudyDay.formattedDate,
                hour = recordState.selectedStudyDay.studyDuration.dayHours,
                minute = recordState.selectedStudyDay.studyDuration.dayMinutes,
            )
        }
        Spacer(Modifier.height(24.dp))
        Row {
            StudyDurationCard(
                title =
                    if (recordState.selectedStudyDay == null) {
                        recordState.calendar.date.year.toString()
                    } else {
                        recordState.selectedStudyDay.studyDay.date.year.toString()
                    },
                studyDurationCardType = StudyDurationCardType.YEAR,
                studyHours = recordState.studyDuration.yearHours,
                studyMinutes = recordState.studyDuration.yearMinutes,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(12.dp))
            StudyDurationCard(
                title =
                    if (recordState.selectedStudyDay == null) {
                        recordState.calendar.date.monthValue.toString()
                    } else {
                        recordState.selectedStudyDay.studyDay.date.monthValue.toString()
                    },
                studyDurationCardType = StudyDurationCardType.MONTH,
                studyHours = recordState.studyDuration.monthHours,
                studyMinutes = recordState.studyDuration.monthMinutes,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(52.dp))
        Hy2Calendar(
            title = recordState.calendar.now,
            isThisMonth = recordState.calendar.isThisMonth,
            days = recordState.calendar.getMonth(),
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            onDayClicked = onDayClicked,
            selectedDay = recordState.selectedStudyDay?.studyDay,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecordScreenPreview() {
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
        RecordScreen(
            recordState = sampleRecordState,
            onPreviousMonthClick = {},
            onNextMonthClick = {},
            onDayClicked = {},
            onRecordShareButtonClick = {},
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = BackgroundBlack),
        )
    }
}
