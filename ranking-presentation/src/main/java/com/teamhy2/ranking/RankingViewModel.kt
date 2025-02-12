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
        override val container: Container<RankingState, RankingSideEffect> = container(RankingState())

        private var currentWeekNumber: Int? = null
        private var latestWeekNumber: Int? = null

        init {
            getWeekNumber()
        }

        private fun getWeekNumber(date: LocalDate = LocalDate.now()) =
            intent {
                reduce { state.copy(isLoading = true) }
                rankingRepository.fetchWeekNumber(date)
                    .onSuccess { weekNumber ->
                        currentWeekNumber = weekNumber.weekNumber
                        latestWeekNumber = weekNumber.weekNumber
                        getDepartmentRankings(weekNumber.weekNumber)
                    }
                    .onFailure { throwable ->
                        postSideEffect(RankingSideEffect.ShowError(throwable))
                    }
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
                            )
                        }
                    }
                    .onFailure { throwable ->
                        postSideEffect(RankingSideEffect.ShowError(throwable))
                    }
            }

        fun getLastWeekRanking() {
            currentWeekNumber?.let { currentWeekNumber ->
                val week: Int = currentWeekNumber % 100
                if (week == 1) return

                getDepartmentRankings(currentWeekNumber - 1)
            }
        }

        fun getNextWeekRanking() {
            currentWeekNumber?.let { currentWeekNumber ->
                if (currentWeekNumber >= (latestWeekNumber ?: currentWeekNumber)) return

                getDepartmentRankings(currentWeekNumber + 1)
            }
        }
    }
