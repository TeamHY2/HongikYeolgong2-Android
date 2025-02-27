package com.teamhy2.ranking

import androidx.lifecycle.ViewModel
import com.teamhy2.ranking.repository.RankingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RankingViewModel
    @Inject
    constructor(
        private val rankingRepository: RankingRepository,
    ) : ViewModel(), ContainerHost<RankingState, RankingSideEffect> {
        override val container: Container<RankingState, RankingSideEffect> =
            container(RankingState())

        private var currentWeekNumber: Int =
            WeekNumberCalculator.calculateWeekNumber(LocalDate.now())
        private val latestWeekNumber: Int = currentWeekNumber

        init {
            getDepartmentRankings(currentWeekNumber)
        }

        private fun getDepartmentRankings(weekNumber: Int) =
            intent {
                reduce { state.copy(isLoading = true) }
                rankingRepository.fetchRanking(weekNumber)
                    .onSuccess { ranking ->
                        currentWeekNumber = weekNumber
                        reduce {
                            state.copy(
                                isLoading = false,
                                currentWeek = ranking.weekName,
                                departmentRankings = ranking.departmentRankings,
                                isNextWeekEnabled = currentWeekNumber < latestWeekNumber,
                            )
                        }
                    }
                    .onFailure { throwable ->
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(RankingSideEffect.ShowError(throwable))
                    }
            }

        fun getLastWeekRanking() {
            val lastWeekNumber: Int = WeekNumberCalculator.shiftWeekNumber(currentWeekNumber, -1)
            getDepartmentRankings(lastWeekNumber)
        }

        fun getNextWeekRanking() {
            if (currentWeekNumber >= latestWeekNumber) return
            val nextWeekNumber = WeekNumberCalculator.shiftWeekNumber(currentWeekNumber, 1)
            getDepartmentRankings(nextWeekNumber)
        }
    }
