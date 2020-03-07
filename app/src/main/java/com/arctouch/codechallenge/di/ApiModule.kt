package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.api.TmdbApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {
    single { createWebService() }
}

fun createWebService(): TmdbApi {
    return Retrofit.Builder()
        .baseUrl(TmdbApi.URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TmdbApi::class.java)
}