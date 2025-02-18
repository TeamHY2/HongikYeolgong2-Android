package com.teamhy2.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.presentation.Hy2Calendar
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.record.components.StudyDurationCard
import com.teamhy2.record.domain.model.StudyDuration
import com.teamhy2.record.model.RecordUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecordRoute(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    val recordUiState by recordViewModel.recordUiState.collectAsStateWithLifecycle()

    val localShowSnackBar = LocalShowSnackBar.current
    val tracker = LocalTracker.current

    LaunchedEffect(true) {
        tracker.trackEvent("Record")
        recordViewModel.errorFlow.collectLatest { throwable ->
            localShowSnackBar.showSnackBar(throwable.message)
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            recordViewModel.loadRecordData()
        }
    }

    RecordScreen(
        recordUiState = recordUiState,
        onPreviousMonthClick = { recordViewModel.updateCalendarMonth(false) },
        onNextMonthClick = { recordViewModel.updateCalendarMonth(true) },
        modifier = modifier,
    )
}

@Composable
fun RecordScreen(
    recordUiState: RecordUiState,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier,
) {
    when (recordUiState) {
        is RecordUiState.Loading -> {
            HY2CircularLoading()
        }

        is RecordUiState.Success -> {
            RecordBody(
                recordUiState = recordUiState,
                onPreviousMonthClick = onPreviousMonthClick,
                onNextMonthClick = onNextMonthClick,
                modifier = modifier.fillMaxSize(),
            )
        }

        is RecordUiState.Error -> Unit
    }
}

@Composable
fun RecordBody(
    recordUiState: RecordUiState,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val calendar = (recordUiState as RecordUiState.Success).calendar

    Column(
        modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 27.dp, bottom = 36.dp),
    ) {
        Hy2Calendar(
            title = calendar.now,
            days = calendar.getMonth(),
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
        )
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Row {
                StudyDurationCard(
                    title = "연간",
                    studyHours = recordUiState.studyDuration.yearHours,
                    studyMinutes = recordUiState.studyDuration.yearMinutes,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row {
                StudyDurationCard(
                    title = "월간",
                    studyHours = recordUiState.studyDuration.monthHours,
                    studyMinutes = recordUiState.studyDuration.monthMinutes,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(13.dp))
                StudyDurationCard(
                    title = "투데이",
                    studyHours = recordUiState.studyDuration.dayHours,
                    studyMinutes = recordUiState.studyDuration.dayMinutes,
                    modifier = Modifier.weight(1f),
                )
            }
        }
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

    val sampleRecordUiState =
        RecordUiState.Success(
            studyDuration = sampleStudySummary,
            calendar = sampleCalendar,
        )

    RecordScreen(
        recordUiState = sampleRecordUiState,
        onPreviousMonthClick = {},
        onNextMonthClick = {},
        modifier = Modifier.fillMaxSize(),
    )
}
