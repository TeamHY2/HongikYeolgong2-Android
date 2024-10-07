package com.teamhy2.ranking

import androidx.lifecycle.ViewModel
import com.teamhy2.ranking.model.RankingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RankingViewModel
    @Inject
    constructor() : ViewModel() {
        private val _rankingUiState = MutableStateFlow(RankingUiState())
        val rankingUiState: StateFlow<RankingUiState> = _rankingUiState.asStateFlow()

        init {
            loadRankingData()
        }

        private fun loadRankingData() {
            // TODO 서버로부터 이번주 랭킹을 가져오는 로직 구현
        }

        fun loadLastWeekRanking() {
            // TODO 서버로부터 이전주 랭킹을 가져오는 로직 구현
        }

        fun loadNextWeekRanking() {
            // TODO 서버로부터 다음주 랭킹을 가져오는 로직 구현
        }
    }
