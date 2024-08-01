package com.teamhy2.feature.setting.data.di

import android.content.Context
import com.teamhy2.feature.setting.data.datasource.LocalSettingsDataSource
import com.teamhy2.feature.setting.data.repository.SettingsRepositoryImpl
import com.teamhy2.feature.setting.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDataSourceModule {
    @Provides
    @Singleton
    fun provideLocalSettingsDataSource(
        @ApplicationContext context: Context,
    ): LocalSettingsDataSource {
        return LocalSettingsDataSource(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}
