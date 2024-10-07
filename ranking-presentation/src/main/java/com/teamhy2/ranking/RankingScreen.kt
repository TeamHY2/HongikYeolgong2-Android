package com.teamhy2.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.ranking.presentation.R
import com.teamhy2.ranking.components.RankingItem
import com.teamhy2.ranking.model.DepartmentRanking
import com.teamhy2.ranking.model.RankingUiState

@Composable
fun RankingRoute(
    modifier: Modifier = Modifier,
    viewModel: RankingViewModel = hiltViewModel(),
) {
    val rankingUiState by viewModel.rankingUiState.collectAsStateWithLifecycle()

    RankingScreen(
        rankingUiState = rankingUiState,
        onLastWeekClick = { viewModel.loadLastWeekRanking() },
        onNextWeekClick = { viewModel.loadNextWeekRanking() },
        modifier = modifier,
    )
}

@Composable
fun RankingScreen(
    rankingUiState: RankingUiState,
    onLastWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    modifier: Modifier =
        Modifier
            .background(BackgroundBlack)
            .padding(horizontal = 24.dp)
            .fillMaxSize(),
) {
    Column(
        modifier = modifier,
    ) {
        RankingHeader(
            rankingUiState = rankingUiState,
            onLastWeekClick = onLastWeekClick,
            onNextWeekClick = onNextWeekClick,
        )
        Spacer(modifier = Modifier.height(20.dp))
        RankingBody(
            rankingUiState = rankingUiState,
        )
    }
}

@Composable
fun RankingHeader(
    rankingUiState: RankingUiState,
    onLastWeekClick: () -> Unit,
    onNextWeekClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 34.dp),
    ) {
        Text(text = rankingUiState.currentWeek, style = HY2Typography().title01, color = Gray100)
        Spacer(modifier = Modifier.weight(1F))
        IconButton(onClick = onLastWeekClick, modifier = Modifier.size(34.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_ranking_last_week),
                contentDescription = null,
            )
        }
        IconButton(onClick = onNextWeekClick, modifier = Modifier.size(34.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_ranking_next_week),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun RankingBody(
    rankingUiState: RankingUiState,
    modifier: Modifier = Modifier,
) {
    val rankingItems = rankingUiState.departmentRankings

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(rankingItems.size) { index ->
            val item = rankingItems[index]
            RankingItem(
                rank = item.rank,
                department = item.departmentName,
                hours = item.totalStudyTime,
                rankChange = item.rankChange,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    val sampleItems =
        listOf(
            DepartmentRanking(1, "국어국문학과", "200H", 1),
            DepartmentRanking(2, "디자인학부", "170H", 1),
            DepartmentRanking(3, "경영학부", "120H", -1),
            DepartmentRanking(4, "건축학부", "80H", 1),
            DepartmentRanking(5, "불어불문학과", "78H", -4),
            DepartmentRanking(6, "사회교육과", "60H", 0),
            DepartmentRanking(7, "수학교육과", "56H", 2),
            DepartmentRanking(8, "국어교육과", "50H", 1),
            DepartmentRanking(9, "체육교육과", "45H", -2),
            DepartmentRanking(10, "음악교육과", "40H", 3),
            DepartmentRanking(11, "영어교육과", "38H", 0),
            DepartmentRanking(12, "역사학과", "35H", 1),
            DepartmentRanking(13, "철학과", "33H", -3),
            DepartmentRanking(14, "미술학부", "30H", 2),
            DepartmentRanking(15, "전자공학과", "28H", 0),
            DepartmentRanking(16, "컴퓨터공학과", "25H", -1),
            DepartmentRanking(17, "화학과", "22H", 3),
            DepartmentRanking(18, "생물학과", "20H", -2),
            DepartmentRanking(19, "지리학과", "18H", 1),
            DepartmentRanking(20, "경제학과", "15H", 2),
        )

    val sampleUiState =
        RankingUiState(
            currentWeek = "9월 1주차",
            departmentRankings = sampleItems,
        )

    RankingScreen(
        rankingUiState = sampleUiState,
        onLastWeekClick = {},
        onNextWeekClick = {},
    )
}