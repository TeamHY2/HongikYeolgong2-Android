package com.teamhy2.feature.setting.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val notificationSwitchState: Flow<Boolean>

    suspend fun saveNotificationSwitchState(isChecked: Boolean)
}
