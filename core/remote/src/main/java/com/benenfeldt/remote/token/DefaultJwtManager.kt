package com.benenfeldt.remote.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultJwtManager
    @Inject
    constructor(private val dataStore: DataStore<Preferences>) :
    JwtManager {
        override suspend fun saveAccessJwt(token: String) {
            dataStore.edit { preferences ->
                preferences[accessJwtKey] = token
            }

            dataStore.data.map { preferences ->
                preferences[accessJwtKey]
            }.firstOrNull()
        }

        override suspend fun getAccessJwt(): String? {
            return dataStore.data.map { preferences ->
                preferences[accessJwtKey]
            }.firstOrNull()
        }

        override suspend fun clearAllTokens() {
            dataStore.edit { preferences ->
                preferences.remove(accessJwtKey)
            }
        }

        companion object {
            private const val ACCESS_JWT_KEY_NAME = "access_jwt"
            private const val REFRESH_JWT_KEY_NAME = "refresh_jwt"
            val accessJwtKey = stringPreferencesKey(ACCESS_JWT_KEY_NAME)
            val refreshJwtKey = stringPreferencesKey(REFRESH_JWT_KEY_NAME)
        }
    }
