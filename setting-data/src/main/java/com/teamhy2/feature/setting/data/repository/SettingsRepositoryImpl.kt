package com.teamhy2.feature.setting.data.repository

import com.teamhy2.feature.setting.data.datasource.LocalSettingsDataSource
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl
    @Inject
    constructor(
        private val localDataSource: LocalSettingsDataSource,
    ) : SettingsRepository {
        override val notificationSwitchState: Flow<Boolean>
            get() = localDataSource.notificationSwitchState

        override suspend fun saveNotificationSwitchState(isChecked: Boolean) {
            localDataSource.saveNotificationSwitchState(isChecked)
        }
    }
