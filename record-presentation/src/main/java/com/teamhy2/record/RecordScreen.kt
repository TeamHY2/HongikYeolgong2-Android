package com.teamhy2.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hongikyeolgong2.calendar.model.Calendar
import com.hongikyeolgong2.calendar.presentation.Hy2Calendar
import com.teamhy2.record.components.StudySummaryCard
import com.teamhy2.record.model.RecordUiState
import com.teamhy2.record.model.StudySummary

@Composable
fun RecordRoute(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    val recordUiState by recordViewModel.recordUiState.collectAsStateWithLifecycle()

    RecordScreen(
        recordUiState = recordUiState,
        onPreviousMonthClick = { recordViewModel.updateCalendarMonth(false) },
        onNextMonthClick = { recordViewModel.updateCalendarMonth(true) },
        modifier = Modifier,
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator()
            }
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
        modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 34.dp, bottom = 36.dp),
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
                StudySummaryCard(
                    title = "연간",
                    studyHours = recordUiState.studySummary.yearHours,
                    studyMinutes = recordUiState.studySummary.yearMinutes,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                StudySummaryCard(
                    title = "이번학기",
                    studyHours = recordUiState.studySummary.semesterHours,
                    studyMinutes = recordUiState.studySummary.semesterMinutes,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                StudySummaryCard(
                    title = "월간",
                    studyHours = recordUiState.studySummary.monthHours,
                    studyMinutes = recordUiState.studySummary.monthMinutes,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                StudySummaryCard(
                    title = "투데이",
                    studyHours = recordUiState.studySummary.dayHours,
                    studyMinutes = recordUiState.studySummary.dayMinutes,
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
        StudySummary(
            yearHours = 200,
            yearMinutes = 4,
            monthHours = 50,
            monthMinutes = 30,
            dayHours = 3,
            dayMinutes = 24,
            semesterHours = 120,
            semesterMinutes = 10,
        )

    val sampleCalendar = Calendar(studyDays = emptyList())

    val sampleRecordUiState =
        RecordUiState.Success(
            studySummary = sampleStudySummary,
            calendar = sampleCalendar,
        )

    RecordScreen(
        recordUiState = sampleRecordUiState,
        onPreviousMonthClick = {},
        onNextMonthClick = {},
        modifier = Modifier.fillMaxSize(),
    )
}
