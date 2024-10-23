package com.benenfeldt.remote.di

import com.benenfeldt.remote.api.UserPublicService
import com.benenfeldt.remote.api.UserService
import com.benenfeldt.remote.token.NeedAuthRetrofit
import com.benenfeldt.remote.token.PublicRetrofit
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
    fun provideUserService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserPublicService(
        @PublicRetrofit retrofit: Retrofit,
    ): UserPublicService {
        return retrofit.create(UserPublicService::class.java)
    }
}
