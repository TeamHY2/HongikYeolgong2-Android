package com.teamhy2.main.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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

                try {
                    val startDate: LocalDate = LocalDate.parse(startDateString, formatter)
                    val endDate: LocalDate = LocalDate.parse(endDateString, formatter)
                    isWithinDateRange(startDate, endDate)
                } catch (e: Exception) {
                    false
                }
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

        private fun isWithinDateRange(
            startDate: LocalDate,
            endDate: LocalDate,
        ): Boolean {
            val today: LocalDate = LocalDate.now()
            return today.isEqual(startDate) || today.isEqual(endDate) ||
                (today.isAfter(startDate) && today.isBefore(endDate))
        }

        companion object Keys {
            val START_DATE: Preferences.Key<String> =
                stringPreferencesKey("promotion_start_date")
            val END_DATE: Preferences.Key<String> =
                stringPreferencesKey("promotion_end_date")
        }
    }
