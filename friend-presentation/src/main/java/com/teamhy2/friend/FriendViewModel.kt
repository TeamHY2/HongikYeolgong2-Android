package com.teamhy2.friend

import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.friend.domain.model.DateType
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
                FriendUiIntent.EnterFriendScreen -> {
                    val dateType =
                        when (current) {
                            is FriendUiState.Loaded -> current.selectedRecordFilterType.toDateType()
                            FriendUiState.Loading -> DateType.MONTHLY
                        }
                    fetchFriends(current, dateType)
                }
                is FriendUiIntent.ChangeRecordFilterType -> {
                    val newState =
                        when (current) {
                            is FriendUiState.Loaded ->
                                current.copy(selectedRecordFilterType = intent.recordFilterType)
                            FriendUiState.Loading ->
                                FriendUiState.Loaded(
                                    selectedRecordFilterType = intent.recordFilterType,
                                    isNotificationOn = false,
                                    friends = emptyList(),
                                )
                        }
                    fetchFriends(newState, intent.recordFilterType.toDateType())
                }
            }
        }

        private suspend fun fetchFriends(
            current: FriendUiState,
            dateType: DateType,
        ): FriendUiState {
            return friendRepository.fetchFriends(dateType)
                .fold(
                    onSuccess = { friends ->
                        when (current) {
                            is FriendUiState.Loaded -> current.copy(friends = friends)
                            FriendUiState.Loading ->
                                FriendUiState.Loaded(
                                    selectedRecordFilterType = dateType.toRecordFilterType(),
                                    isNotificationOn = false,
                                    friends = friends,
                                )
                        }
                    },
                    onFailure = {
                        postSideEffect(FriendSideEffect.ShowSnackBar(it.message ?: "알 수 없는 오류가 발생했습니다."))
                        current
                    },
                )
        }
    }
