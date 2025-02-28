package com.teamhy2.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teamhy2.designsystem.common.HY2LoadingScreen
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.hongikyeolgong2.ranking.presentation.R
import com.teamhy2.ranking.components.RankingItem
import com.teamhy2.ranking.model.DepartmentRanking
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RankingRoute(
    modifier: Modifier = Modifier,
    rankingViewModel: RankingViewModel = hiltViewModel(),
) {
    val state: RankingState by rankingViewModel.collectAsState()
    val localShowSnackBar = LocalShowSnackBar.current
    val tracker = LocalTracker.current

    rankingViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RankingSideEffect.ShowError -> {
                localShowSnackBar.showSnackBar(sideEffect.throwable.message)
            }
        }
    }

    LaunchedEffect(true) {
        tracker.trackEvent("Ranking")
    }

    RankingScreen(
        isLoading = state.isLoading,
        currentWeek = state.currentWeek,
        departmentRankings = state.departmentRankings.toImmutableList(),
        isNextWeekEnabled = state.isNextWeekEnabled,
        onLastWeekClick = { rankingViewModel.getLastWeekRanking() },
        onNextWeekClick = { rankingViewModel.getNextWeekRanking() },
        modifier =
            modifier
                .background(BackgroundBlack)
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
    )
}

@Composable
fun RankingScreen(
    isLoading: Boolean,
    currentWeek: String,
    departmentRankings: ImmutableList<DepartmentRanking>,
    isNextWeekEnabled: Boolean,
    onLastWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        RankingHeader(
            currentWeek = currentWeek,
            onLastWeekClick = onLastWeekClick,
            onNextWeekClick = onNextWeekClick,
            isNextWeekEnabled = isNextWeekEnabled,
        )
        Spacer(modifier = Modifier.height(20.dp))
        when (isLoading) {
            true -> HY2LoadingScreen()
            false -> RankingBody(departmentRankings = departmentRankings)
        }
    }
}

@Composable
fun RankingHeader(
    currentWeek: String,
    isNextWeekEnabled: Boolean,
    onLastWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 27.dp),
    ) {
        Text(text = currentWeek, style = HY2Typography().title01, color = Gray100)
        Spacer(modifier = Modifier.weight(1F))
        IconButton(onClick = onLastWeekClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_ranking_last_week),
                contentDescription = null,
            )
        }
        IconButton(
            onClick = onNextWeekClick,
            enabled = isNextWeekEnabled,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_ranking_next_week),
                contentDescription = null,
                colorFilter = if (isNextWeekEnabled.not()) ColorFilter.tint(Gray800) else null,
            )
        }
    }
}

@Composable
fun RankingBody(
    departmentRankings: ImmutableList<DepartmentRanking>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 10.dp),
    ) {
        items(departmentRankings, key = { it.department }) { item ->
            RankingItem(
                rank = item.currentRank,
                departmentName = item.department,
                hours = item.studyDurationOfWeek,
                rankChange = item.rankChange,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    val sampleDepartmentRankings =
        listOf(
            DepartmentRanking(
                department = "국어국문학과",
                studyDurationOfWeek = 200,
                currentRank = 1,
                rankChange = 1,
            ),
            DepartmentRanking(
                department = "디자인학부",
                studyDurationOfWeek = 170,
                currentRank = 2,
                rankChange = 1,
            ),
            DepartmentRanking(
                department = "경영학부",
                studyDurationOfWeek = 120,
                currentRank = 3,
                rankChange = -1,
            ),
            DepartmentRanking(
                department = "건축학부",
                studyDurationOfWeek = 80,
                currentRank = 4,
                rankChange = 1,
            ),
            DepartmentRanking(
                department = "불어불문학과",
                studyDurationOfWeek = 78,
                currentRank = 5,
                rankChange = -4,
            ),
            DepartmentRanking(
                department = "사회교육과",
                studyDurationOfWeek = 60,
                currentRank = 6,
                rankChange = 0,
            ),
            DepartmentRanking(
                department = "수학교육과",
                studyDurationOfWeek = 56,
                currentRank = 7,
                rankChange = 2,
            ),
            DepartmentRanking(
                department = "국어교육과",
                studyDurationOfWeek = 50,
                currentRank = 8,
                rankChange = 1,
            ),
            DepartmentRanking(
                department = "체육교육과",
                studyDurationOfWeek = 45,
                currentRank = 9,
                rankChange = -2,
            ),
            DepartmentRanking(
                department = "음악교육과",
                studyDurationOfWeek = 40,
                currentRank = 10,
                rankChange = 3,
            ),
        ).toImmutableList()

    RankingScreen(
        isLoading = false,
        currentWeek = "9월 1주차",
        departmentRankings = sampleDepartmentRankings,
        onLastWeekClick = {},
        onNextWeekClick = {},
        isNextWeekEnabled = true,
    )
}
