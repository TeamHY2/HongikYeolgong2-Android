package com.teamhy2.ranking

import app.cash.turbine.test
import com.teamhy2.ranking.model.Ranking
import com.teamhy2.ranking.repository.RankingRepository
import com.teamhy2.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class RankingViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: RankingViewModel
    private val rankingRepository: RankingRepository = mockk()
    private lateinit var weekNumberCalculator: WeekNumberCalculator

    @Test
    fun `RankingViewModel의 초기 상태는 Loading이다`() =
        runTest {
            // given & when
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2025, 1, 1))
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            // then
            viewModel.uiState.test {
                val actual: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = actual)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `주 번호를 기준으로 랭킹을 가져올 수 있다`() =
        runTest {
            // given
            val weekNumber = 202501
            val weekName = "1월 1주차"
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2025, 1, 1))
            coEvery { rankingRepository.fetchRanking(weekNumber) } returns
                Result.success(Ranking(weekName, mockDepartmentRankings))
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            viewModel.uiState.test {
                val initState: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = initState)

                // when
                viewModel.sendIntent(RankingIntent.EnterRankingScreen)
                val actual: RankingUiState = awaitItem()

                // then
                assertEquals(
                    expected =
                        RankingUiState.Loaded(
                            currentWeek = weekName,
                            departmentRankings = mockDepartmentRankings.toImmutableList(),
                            canMoveToPreviousWeek = weekNumberCalculator.isMinimumWeekNumber.not(),
                            canMoveToNextWeek = weekNumberCalculator.isMaximumWeekNumber.not(),
                        ),
                    actual = actual,
                )
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { rankingRepository.fetchRanking(weekNumber) }
        }

    @Test
    fun `현재 주 번호가 최소 주 번호라면 이전 주로 이동하려 할 때 최근 상태를 그대로 유지한다`() =
        runTest {
            // given
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2024, 1, 1))
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            viewModel.uiState.test {
                val initState: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = initState)

                // when
                viewModel.sendIntent(RankingIntent.MoveToPreviousMonth)

                // then: 최근 상태를 그대로 유지하기 때문에 UI State는 갱신되지 않음
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `현재 주 번호가 최대 주 번호라면 다음 주로 이동하려 할 때 최근 상태를 그대로 유지한다`() =
        runTest {
            // given: 오늘 날짜가 2025년 1월 1일 이라고 가정
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2025, 1, 1))
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            viewModel.uiState.test {
                val initState: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = initState)

                // when: 오늘 날짜보다 다음 주로 넘어가려하면
                viewModel.sendIntent(RankingIntent.MoveToNextMonth)

                // then: 최대 주 번호이기 때문에 최근 상태를 그대로 유지하기 때문에 UI State는 갱신되지 않음
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }
}
