package com.arctouch.codechallenge.di

import org.koin.dsl.module

val repositoryModule = module {
    single<TmbRepository> { TmbRepositoryImpl(get()) }
}