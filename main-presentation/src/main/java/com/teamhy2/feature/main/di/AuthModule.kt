package com.teamhy2.feature.main.di

import com.benenfeldt.remote.token.AuthCallback
import com.teamhy2.feature.main.MainAuthCallback
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthCallback(mainAuthCallback: MainAuthCallback): AuthCallback
}
