package com.benenfeldt.remote.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.benenfeldt.remote.token.DefaultJwtManager
import com.benenfeldt.remote.token.JwtManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JwtModule {
    private const val AUTH_PREFERENCE = "authPreference"

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler =
                ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() },
                ),
            produceFile = { context.preferencesDataStoreFile(AUTH_PREFERENCE) },
        )
    }

    @Provides
    @Singleton
    fun provideJwtTokenManager(dataStore: DataStore<Preferences>): JwtManager {
        return DefaultJwtManager(dataStore = dataStore)
    }
}
