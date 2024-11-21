package com.teamhy2.tracker.di

import android.content.Context
import com.teamhy2.hongikyeolgong2.tracker.BuildConfig.AMPLITUDE_KEY
import com.teamhy2.tracker.Tracker
import com.teamhy2.tracker.amplitude.AmplitudeTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerModule {
    @Provides
    @Singleton
    fun provideTracker(
        @ApplicationContext context: Context,
    ): Tracker {
        return AmplitudeTracker(context, AMPLITUDE_KEY)
    }
}
