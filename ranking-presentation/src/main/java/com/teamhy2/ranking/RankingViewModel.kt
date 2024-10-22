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
        private lateinit var _rankingUiState: MutableStateFlow<RankingUiState>
        val rankingUiState: StateFlow<RankingUiState>
            get() = _rankingUiState.asStateFlow()

        init {
            loadRankingData()
        }

        private fun loadRankingData() {
            // TODO: 랭킹을 서버로부터 불러 오는 로직을 구현
        }

        fun loadLastWeekRanking() {
            // TODO: 이전 주 랭킹을 불러오는 로직 구현
        }

        fun loadNextWeekRanking() {
            // TODO: 다음 주 랭킹을 불러오는 로직 구현
        }
    }
