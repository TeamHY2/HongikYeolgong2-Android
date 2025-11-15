package com.teamhy2.friend

import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.friend.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendViewModel
    @Inject
    constructor(
        private val friendRepository: FriendRepository,
    ) : MviViewModel<FriendUiIntent, FriendUiState, FriendSideEffect>(FriendUiState.Loading) {
        override suspend fun reduceState(
            current: FriendUiState,
            intent: FriendUiIntent,
        ): FriendUiState {
            return when (intent) {
                FriendUiIntent.EnterFriendScreen -> fetchFriends(current)
            }
        }

        private suspend fun fetchFriends(current: FriendUiState): FriendUiState {
            return friendRepository.fetchFriends()
                .fold(
                    onSuccess = { friends -> FriendUiState.Loaded(friends) },
                    onFailure = {
                        postSideEffect(FriendSideEffect.ShowSnackBar(it.message ?: "알 수 없는 오류가 발생했습니다."))
                        current
                    },
                )
        }
    }
