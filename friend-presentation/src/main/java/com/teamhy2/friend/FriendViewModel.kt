package com.teamhy2.friend

import com.teamhy2.designsystem.util.mvi.MviViewModel
import com.teamhy2.friend.domain.model.DateType
import com.teamhy2.friend.domain.model.Friend
import com.teamhy2.friend.domain.repository.FriendRepository
import com.teamhy2.notification.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class FriendViewModel
    @Inject
    constructor(
        private val friendRepository: FriendRepository,
        private val notificationRepository: NotificationRepository,
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
                    fetchFriendsAndNotifications(
                        current = current,
                        dateType = dateType,
                    )
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
                    fetchFriends(
                        current = newState,
                        dateType = intent.recordFilterType.toDateType(),
                    )
                }

                FriendUiIntent.MarkNotificationsAsRead -> {
                    markNotificationsAsRead(current)
                }
            }
        }

        private suspend fun fetchFriendsAndNotifications(
            current: FriendUiState,
            dateType: DateType,
        ): FriendUiState =
            coroutineScope {
                runCatching {
                    val friendsDeferred: Deferred<List<Friend>> =
                        async { friendRepository.fetchFriends(dateType).getOrThrow() }
                    val hasUnreadNotificationsDeferred: Deferred<Boolean> =
                        async { notificationRepository.hasUnreadNotifications().getOrThrow() }

                    val friends: List<Friend> = friendsDeferred.await()
                    val isNotificationOn = hasUnreadNotificationsDeferred.await()

                    when (current) {
                        is FriendUiState.Loaded -> current.copy(isNotificationOn = isNotificationOn)
                        FriendUiState.Loading ->
                            FriendUiState.Loaded(
                                selectedRecordFilterType = dateType.toRecordFilterType(),
                                isNotificationOn = isNotificationOn,
                                friends = friends,
                            )
                    }
                }.fold(
                    onSuccess = { it },
                    onFailure = {
                        postSideEffect(
                            FriendSideEffect.ShowSnackBar(
                                it.message ?: "알 수 없는 오류가 발생했습니다.",
                            ),
                        )
                        current
                    },
                )
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
                        postSideEffect(
                            FriendSideEffect.ShowSnackBar(
                                it.message ?: "알 수 없는 오류가 발생했습니다.",
                            ),
                        )
                        current
                    },
                )
        }

        private suspend fun markNotificationsAsRead(current: FriendUiState): FriendUiState {
            return runCatching {
                notificationRepository.markNotificationsAsRead().getOrThrow()

                when (current) {
                    is FriendUiState.Loaded -> current.copy(isNotificationOn = false)
                    FriendUiState.Loading -> current
                }
            }.fold(
                onSuccess = { it },
                onFailure = {
                    postSideEffect(
                        FriendSideEffect.ShowSnackBar(
                            it.message ?: "알 수 없는 오류가 발생했습니다.",
                        ),
                    )
                    current
                },
            )
        }
    }
