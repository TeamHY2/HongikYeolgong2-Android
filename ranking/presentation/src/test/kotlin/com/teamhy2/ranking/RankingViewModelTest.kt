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
import java.io.IOException
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
    fun `랭킹을 가져올 수 없다면 에러 메시지 표시를 요청한다`() =
        runTest {
            // given: 서버 통신이 불가능한 상황에서
            val weekNumber = 202501
            val serverError = IOException("서버 통신 에러")
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2025, 1, 1))
            coEvery { rankingRepository.fetchRanking(weekNumber) } returns
                Result.failure(serverError)
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            viewModel.uiState.test {
                val initState: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = initState)

                // when: 랭킹을 가져오려고 시도한다면
                viewModel.sendIntent(RankingIntent.EnterRankingScreen)

                // 더이상 UiState를 방출하지 않음
                expectNoEvents()
            }

            viewModel.sideEffect.test {
                val actual: RankingSideEffect = awaitItem()

                // then: 랭킹을 가져오지 못했다는 에러 메시지를 요청
                assertEquals(
                    expected = RankingSideEffect.ShowError(serverError),
                    actual = actual,
                )
            }
            coVerify(exactly = 1) { rankingRepository.fetchRanking(weekNumber) }
        }

    @Test
    fun `현재 주에서 이전 주로 이동할 수 있다`() =
        runTest {
            // given: 2024년 6월 1일이 현재라 가정할때 주 번호는 202422
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2024, 6, 1))
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            // 이전 주의 주번호는 202421
            coEvery { rankingRepository.fetchRanking(202421) } returns
                Result.success(Ranking("5월 4주차", mockDepartmentRankings))

            viewModel.uiState.test {
                val initState: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = initState)

                // when
                viewModel.sendIntent(RankingIntent.MoveToPreviousMonth)

                // then
                val actual = awaitItem()
                assertEquals(
                    expected =
                        RankingUiState.Loaded(
                            currentWeek = "5월 4주차",
                            departmentRankings = mockDepartmentRankings.toImmutableList(),
                            canMoveToPreviousWeek = true,
                            canMoveToNextWeek = true,
                        ),
                    actual = actual,
                )
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { rankingRepository.fetchRanking(202421) }
        }

    @Test
    fun `현재 주에서 다음 주로 이동할 수 있다`() =
        runTest {
            // given: 2024년 6월 1일이 현재라 가정할때 주 번호는 202422
            weekNumberCalculator = WeekNumberCalculator(LocalDate.of(2024, 6, 1))
            viewModel = RankingViewModel(rankingRepository, weekNumberCalculator)

            // 현재 주가 바로 Maximum 주라 다음 주로 이동이 불가능하기 때문에 테스트를 위해 이전 주로 두번 이동 필요
            coEvery { rankingRepository.fetchRanking(202421) } returns
                Result.success(Ranking("5월 4주차", mockDepartmentRankings))
            coEvery { rankingRepository.fetchRanking(202420) } returns
                Result.success(Ranking("5월 3주차", mockDepartmentRankings))

            viewModel.uiState.test {
                val initState: RankingUiState = awaitItem()
                assertEquals(expected = RankingUiState.Loading, actual = initState)

                viewModel.sendIntent(RankingIntent.MoveToPreviousMonth)
                viewModel.sendIntent(RankingIntent.MoveToPreviousMonth)
                awaitItem()
                awaitItem()

                // when: 다음 주로 이동하려고 시도한다면
                viewModel.sendIntent(RankingIntent.MoveToNextMonth)
                assertEquals(weekNumberCalculator.currentWeekNumber, 202421)

                // then: 이전 주로 두번 이동하고 다음 주로 한 번 이동하기 때문에 결과적으로 5월 4주차 데이터 방출
                val actual = awaitItem()
                assertEquals(
                    expected =
                        RankingUiState.Loaded(
                            currentWeek = "5월 4주차",
                            departmentRankings = mockDepartmentRankings.toImmutableList(),
                            canMoveToPreviousWeek = true,
                            canMoveToNextWeek = true,
                        ),
                    actual = actual,
                )
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 2) { rankingRepository.fetchRanking(202421) }
            coVerify(exactly = 1) { rankingRepository.fetchRanking(202420) }
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
