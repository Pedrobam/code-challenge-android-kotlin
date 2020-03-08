package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.api.TmdbApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {
    single { provideDefaultOkhttpClient() }
    single { provideRetrofit(get()) }
}

fun provideDefaultOkhttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            chain.proceed(request)
        }
        .build()
}

fun provideRetrofit(client: OkHttpClient): TmdbApi {
    return Retrofit.Builder()
        .baseUrl(TmdbApi.URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(TmdbApi::class.java)
}