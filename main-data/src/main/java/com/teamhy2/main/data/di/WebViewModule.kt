package com.teamhy2.main.data.di

import com.teamhy2.main.data.repository.DefaultWebViewRepository
import com.teamhy2.main.domain.WebViewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebViewModule {
    @Binds
    @Singleton
    abstract fun bindWebViewRepository(impl: DefaultWebViewRepository): WebViewRepository
}
