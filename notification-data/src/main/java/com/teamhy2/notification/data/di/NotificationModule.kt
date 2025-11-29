package com.teamhy2.notification.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.teamhy2.notification.data.repository.RemoteNotificationRepository
import com.teamhy2.notification.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationDataStore

@Module
@InstallIn(SingletonComponent::class)
object NotificationDataStoreModule {
    private const val NOTIFICATION_PREFERENCE_NAME = "notification_preference"

    @Provides
    @Singleton
    @NotificationDataStore
    fun provideNotificationDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler =
                ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() },
                ),
            produceFile = { context.preferencesDataStoreFile(NOTIFICATION_PREFERENCE_NAME) },
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: RemoteNotificationRepository): NotificationRepository
}
