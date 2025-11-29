package com.teamhy2.friend

import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState
import com.teamhy2.friend.domain.model.DateType
import com.teamhy2.friend.domain.model.Friend

sealed interface FriendUiState : UiState {
    data object Loading : FriendUiState

    data class Loaded(
        val selectedRecordFilterType: RecordFilterType,
        val isNotificationOn: Boolean,
        val friends: List<Friend>,
    ) : FriendUiState
}

sealed interface FriendUiIntent : UiIntent {
    data object EnterFriendScreen : FriendUiIntent

    data class ChangeRecordFilterType(val recordFilterType: RecordFilterType) : FriendUiIntent
}

sealed interface FriendSideEffect : SideEffect {
    data class ShowSnackBar(val message: String) : FriendSideEffect
}

enum class RecordFilterType {
    MONTHLY,
    DAILY,
    ;

    fun toggled(): RecordFilterType {
        return when (this) {
            MONTHLY -> DAILY
            DAILY -> MONTHLY
        }
    }

    fun toDateType(): DateType {
        return when (this) {
            MONTHLY -> DateType.MONTHLY
            DAILY -> DateType.DAILY
        }
    }
}

fun DateType.toRecordFilterType(): RecordFilterType {
    return when (this) {
        DateType.MONTHLY -> RecordFilterType.MONTHLY
        DateType.DAILY -> RecordFilterType.DAILY
    }
}
