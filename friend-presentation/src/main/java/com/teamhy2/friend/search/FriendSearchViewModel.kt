package com.teamhy2.friend.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.domain.repository.FriendRepository
import com.teamhy2.friend.search.model.SearchResultItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class FriendSearchViewModel
    @Inject
    constructor(
        private val repository: FriendRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FriendSearchUiState())
        val uiState: StateFlow<FriendSearchUiState> = _uiState.asStateFlow()

        private val _effect = Channel<FriendSearchEffect>(Channel.BUFFERED)
        val effect = _effect.receiveAsFlow()

        init {
            viewModelScope.launch {
                uiState
                    .map { it.query }
                    .debounce(1000)
                    .distinctUntilChanged()
                    .collect { q ->
                        if (q.isBlank()) {
                            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
                        } else {
                            _uiState.update { it.copy(isLoading = true) }
                            repository.searchFriends(q)
                                .onSuccess { list ->
                                    _uiState.update {
                                        it.copy(
                                            results =
                                                list.map { item ->
                                                    SearchResultItemUiModel(
                                                        userId = item.userId,
                                                        nickname = item.nickname,
                                                        friendStatus = item.friendStatus,
                                                    )
                                                },
                                            isLoading = false,
                                        )
                                    }
                                }
                                .onFailure { e ->
                                    _uiState.update {
                                        it.copy(
                                            results = emptyList(),
                                            isLoading = false,
                                        )
                                    }
                                    _effect.send(
                                        FriendSearchEffect.ShowSnackBar(
                                            e.message ?: "알 수 없는 오류가 발생했습니다.",
                                        ),
                                    )
                                }
                        }
                    }
            }
        }

        fun onQueryChange(value: String) {
            _uiState.update { it.copy(query = value) }
        }

        fun clearQuery() {
            _uiState.update { it.copy(query = "") }
        }

        fun onBackClicked() {
            viewModelScope.launch { _effect.send(FriendSearchEffect.NavigateToFriend) }
        }

        fun onFriendStatusButtonClick(
            userId: Long,
            nickname: String,
            status: FriendStatus,
        ) {
            if (status != FriendStatus.NONE) return
            viewModelScope.launch {
                repository.addNewFriend(userId)
                    .onSuccess { newStatus ->
                        _uiState.update { state ->
                            state.copy(
                                results =
                                    state.results.map { item ->
                                        if (item.userId == userId) item.copy(friendStatus = newStatus) else item
                                    },
                            )
                        }
                        _effect.send(
                            FriendSearchEffect.ShowSnackBar("${nickname}에게 친구추가 요청을 보냈어요."),
                        )
                    }
                    .onFailure { e ->
                        _effect.send(
                            FriendSearchEffect.ShowSnackBar(e.message ?: "알 수 없는 오류가 발생했습니다."),
                        )
                    }
            }
        }
    }
