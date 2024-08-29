package com.teamhy2.main.data.di

import com.teamhy2.main.data.repository.DefaultStudyDayRepository
import com.teamhy2.main.data.repository.DefaultWebViewRepository
import com.teamhy2.main.data.repository.DefaultWiseSayingRepository
import com.teamhy2.main.domain.repository.StudyDayRepository
import com.teamhy2.main.domain.repository.WebViewRepository
import com.teamhy2.main.domain.repository.WiseSayingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindWebViewRepository(impl: DefaultWebViewRepository): WebViewRepository

    @Binds
    @Singleton
    abstract fun bindWiseSayingRepository(impl: DefaultWiseSayingRepository): WiseSayingRepository

    @Binds
    @Singleton
    abstract fun bindStudyDayRepository(impl: DefaultStudyDayRepository): StudyDayRepository
}
