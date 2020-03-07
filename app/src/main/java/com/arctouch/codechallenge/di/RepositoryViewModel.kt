package com.arctouch.codechallenge.di

import org.koin.dsl.module

val repositoryViewModel = module {
    single { TmbRepositoryImpl(get()) }
}