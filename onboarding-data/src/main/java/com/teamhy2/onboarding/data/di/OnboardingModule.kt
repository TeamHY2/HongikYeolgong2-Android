package com.teamhy2.onboarding.data.di

import com.teamhy2.onboarding.data.repository.DefaultWebViewRepository
import com.teamhy2.onboarding.domain.repository.WebViewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingModule {
    @Binds
    @Singleton
    abstract fun bindWebViewRepository(impl: DefaultWebViewRepository): WebViewRepository
}
