package com.benenfeldt.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): ApiModule {
        return retrofit.create(ApiModule::class.java)
    }
}
