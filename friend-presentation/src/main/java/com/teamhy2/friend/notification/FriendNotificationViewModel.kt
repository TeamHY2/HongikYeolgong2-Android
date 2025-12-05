package com.teamhy2.friend.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamhy2.friend.domain.model.FriendStatus
import com.teamhy2.friend.domain.repository.FriendNotificationRepository
import com.teamhy2.notification.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendNotificationViewModel @Inject constructor(
    private val repository: FriendNotificationRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendNotificationUiState(isLoading = true))
    val uiState: StateFlow<FriendNotificationUiState> = _uiState.asStateFlow()

    private val _effect = Channel<FriendNotificationEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        refresh()
    }

    fun onBackClicked() {
        viewModelScope.launch { _effect.send(FriendNotificationEffect.NavigateBack) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.fetchNotifications()
                .onSuccess { list ->
                    notificationRepository.markNotificationsAsRead()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = list.toImmutableList(),
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(
                        FriendNotificationEffect.ShowSnackBar(
                            e.message ?: "알 수 없는 오류가 발생했습니다.",
                        ),
                    )
                }
        }
    }

    fun onAccept(
        notificationId: Long,
        friendId: Long,
        senderId: Long,
    ) {
        handleAction(notificationId, friendId, senderId, FriendStatus.ACCEPTED)
    }

    fun onReject(
        notificationId: Long,
        friendId: Long,
        senderId: Long,
    ) {
        handleAction(notificationId, friendId, senderId, FriendStatus.REJECTED)
    }

    private fun handleAction(
        notificationId: Long,
        friendId: Long,
        senderId: Long,
        status: FriendStatus,
    ) {
        viewModelScope.launch {
            repository.updateFriendRequest(notificationId, friendId, senderId, status)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            notifications =
                                state.notifications.filterNot { it.id == notificationId }
                                    .toImmutableList(),
                        )
                    }
                    val text =
                        if (status == FriendStatus.ACCEPTED) "친구 요청을 수락했어요." else "요청을 거절했어요."
                    _effect.send(FriendNotificationEffect.ShowSnackBar(text))
                }
                .onFailure { e ->
                    _effect.send(
                        FriendNotificationEffect.ShowSnackBar(
                            e.message ?: "요청 처리 중 오류가 발생했어요.",
                        ),
                    )
                }
        }
    }
}
