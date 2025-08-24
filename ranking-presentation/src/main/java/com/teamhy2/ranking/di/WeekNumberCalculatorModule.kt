package com.teamhy2.ranking.di

import com.teamhy2.ranking.WeekNumberCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WeekNumberCalculatorModule {
    @Provides
    fun provideWeekNumberCalculator(): WeekNumberCalculator {
        return WeekNumberCalculator()
    }
}
