package com.teamhy2.main.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.teamhy2.main.domain.util.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PromotionDataStore
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

        val isPromotionDismissed: Flow<Boolean> =
            dataStore.data.map { preferences ->
                val startDateString: String? = preferences[START_DATE]
                val endDateString: String? = preferences[END_DATE]
                if (startDateString == null || endDateString == null) return@map false

                DateUtil.isTodayWithinDateRange(startDateString = startDateString, endDateString = endDateString)
            }

        suspend fun savePromotionDismissPeriod(
            startDate: LocalDate,
            endDate: LocalDate,
        ) {
            dataStore.edit { preferences: MutablePreferences ->
                preferences[START_DATE] = startDate.format(formatter)
                preferences[END_DATE] = endDate.format(formatter)
            }
        }

        companion object Keys {
            private const val PROMOTION_START_DATE_KEY = "promotion_start_date"
            private const val PROMOTION_END_DATE_KEY = "promotion_end_date"

            val START_DATE: Preferences.Key<String> =
                stringPreferencesKey(PROMOTION_START_DATE_KEY)
            val END_DATE: Preferences.Key<String> =
                stringPreferencesKey(PROMOTION_END_DATE_KEY)
        }
    }
