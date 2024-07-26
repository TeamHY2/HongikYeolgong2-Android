package com.teamhy2.feature.setting.data.di

import android.content.Context
import com.teamhy2.feature.setting.data.datasource.SettingsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SettingModule {
    @Provides
    fun provideSettingsDataSource(
        @ApplicationContext context: Context,
    ): SettingsDataSource {
        return SettingsDataSource(context)
    }
}
