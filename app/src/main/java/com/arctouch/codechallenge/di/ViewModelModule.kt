package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.ui.home.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { HomeViewModel(get()) }
}