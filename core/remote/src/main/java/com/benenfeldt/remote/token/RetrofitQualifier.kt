package com.benenfeldt.remote.token

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PublicClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PublicRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedAuthClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedAuthRetrofit
