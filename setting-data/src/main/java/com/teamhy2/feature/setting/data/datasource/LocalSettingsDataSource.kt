package com.teamhy2.feature.setting.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "settings_prefs"

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class LocalSettingsDataSource
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        companion object {
            private val NOTIFICATION_SWITCH_STATE_KEY =
                booleanPreferencesKey("notification_switch_state")
        }

        val notificationSwitchState: Flow<Boolean> =
            context.dataStore.data
                .map { preferences ->
                    preferences[NOTIFICATION_SWITCH_STATE_KEY] ?: false
                }

        suspend fun saveNotificationSwitchState(isChecked: Boolean) {
            context.dataStore.edit { preferences ->
                preferences[NOTIFICATION_SWITCH_STATE_KEY] = isChecked
            }
        }
    }
