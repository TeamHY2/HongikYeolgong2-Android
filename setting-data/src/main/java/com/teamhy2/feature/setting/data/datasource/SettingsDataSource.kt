package com.teamhy2.feature.setting.data.datasource

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsDataSource
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        companion object {
            private const val PREFS_NAME = "settings_prefs"
            private const val NOTIFICATION_SWITCH_STATE_KEY = "notification_switch_state"
        }

        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        fun getNotificationSwitchState(): Boolean {
            return sharedPreferences.getBoolean(NOTIFICATION_SWITCH_STATE_KEY, false)
        }

        fun saveNotificationSwitchState(isChecked: Boolean) {
            sharedPreferences.edit().putBoolean(NOTIFICATION_SWITCH_STATE_KEY, isChecked).apply()
        }
    }
