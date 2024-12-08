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
                requireNotNull(startDateString) { ERROR_START_DATE_MISSING }
                requireNotNull(endDateString) { ERROR_END_DATE_MISSING }

                val startDate: LocalDate = LocalDate.parse(startDateString, formatter)
                val endDate: LocalDate = LocalDate.parse(endDateString, formatter)

                DateUtil.isTodayWithinDateRange(startDate = startDate, endDate = endDate)
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

            const val ERROR_START_DATE_MISSING = "DataStore에 시작 날짜가 없습니다."
            const val ERROR_END_DATE_MISSING = "DataStore에 종료 날짜가 없습니다."

            val START_DATE: Preferences.Key<String> =
                stringPreferencesKey(PROMOTION_START_DATE_KEY)
            val END_DATE: Preferences.Key<String> =
                stringPreferencesKey(PROMOTION_END_DATE_KEY)
        }
    }
