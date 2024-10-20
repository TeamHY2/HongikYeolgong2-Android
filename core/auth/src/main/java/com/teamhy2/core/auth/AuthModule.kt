package com.teamhy2.core.auth

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun providesGoogleLogin(
        @ApplicationContext context: Context,
    ): GoogleSignIn {
        return GoogleSignIn(
            context = context,
        )
    }
}
