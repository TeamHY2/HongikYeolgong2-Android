package com.teamhy2.friend.data.di

import com.benenfeldt.remote.api.FriendService
import com.benenfeldt.remote.token.NeedAuthRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FriendSearchProvideModule {
    @Provides
    @Singleton
    fun provideFriendService(
        @NeedAuthRetrofit retrofit: Retrofit,
    ): FriendService = retrofit.create(FriendService::class.java)
}
