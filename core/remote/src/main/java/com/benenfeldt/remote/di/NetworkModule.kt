package com.benenfeldt.remote.di

import android.util.Log
import com.benenfeldt.remote.BuildConfig
import com.benenfeldt.remote.token.AccessTokenInterceptor
import com.benenfeldt.remote.token.NeedAuthClient
import com.benenfeldt.remote.token.NeedAuthRetrofit
import com.benenfeldt.remote.token.PublicClient
import com.benenfeldt.remote.token.PublicRetrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CONTENT_TYPE = "application/json"
    private const val CONNECT_TIMEOUT = 20L
    private const val WRITE_TIMEOUT = 20L
    private const val READ_TIMEOUT = 20L
    private const val TAG_HTTP_LOG = "Http_Log"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Log.d(TAG_HTTP_LOG, message)
        }.apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    @Provides
    @Singleton
    @PublicClient
    fun providePublicOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @PublicRetrofit
    fun providePublicRetrofit(
        @PublicClient okHttpClient: OkHttpClient,
    ): Retrofit {
        val jsonConfig = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(jsonConfig.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @NeedAuthClient
    fun provideAuthOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @NeedAuthRetrofit
    fun provideAuthRetrofit(
        @NeedAuthClient okHttpClient: OkHttpClient,
    ): Retrofit {
        val jsonConfig = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(jsonConfig.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}
