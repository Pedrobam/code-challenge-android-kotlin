package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.ui.home.HomePagedViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { HomePagedViewModel(get()) }
}