package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.ui.details.DetailsViewModel
import com.arctouch.codechallenge.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}