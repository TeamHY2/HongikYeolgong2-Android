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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.record.components.StudyDurationCard
import com.teamhy2.record.components.StudyDurationCardType
import com.teamhy2.record.domain.model.StudyDuration
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

@Composable
fun RecordRoute(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    val recordState by recordViewModel.collectAsState()

    val localShowSnackBar = LocalShowSnackBar.current
    val tracker = LocalTracker.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

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
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
fun RecordScreen(
    recordState: RecordState,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDayClicked: (StudyDay?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp),
    ) {
        if (recordState.selectedStudyDay == null) {
            DatePanel(
                date = recordState.date,
                hour = recordState.studyDuration.dayHours,
                minute = recordState.studyDuration.dayMinutes,
            )
        } else {
            DatePanel(
                date = recordState.selectedStudyDay.date,
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
            isThisMonth = recordState.calendar.date == LocalDate.now(),
            days = recordState.calendar.getMonth(),
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            onDayClicked = onDayClicked,
            selectedDay = recordState.selectedStudyDay?.studyDay,
        )
    }
}

@Composable
private fun DatePanel(
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
            text = "${hour}H ${minute}M",
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

    RecordScreen(
        recordState = sampleRecordState,
        onPreviousMonthClick = {},
        onNextMonthClick = {},
        onDayClicked = {},
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = BackgroundBlack),
    )
}
