package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.feature.details.DetailsViewModel
import com.arctouch.codechallenge.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}