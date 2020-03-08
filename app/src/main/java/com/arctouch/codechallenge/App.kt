package com.arctouch.codechallenge

import android.app.Application
import android.util.Log
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.di.TmbRepository
import com.arctouch.codechallenge.di.apiModule
import com.arctouch.codechallenge.di.repositoryModule
import com.arctouch.codechallenge.di.viewModelModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject

class App : Application(), KoinComponent {

    private val repository: TmbRepository by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(apiModule, repositoryModule, viewModelModule))
        }
        getGenres()
    }

    private fun getGenres() {
        GlobalScope.launch {
            try {
                val response = repository.genres()
                Cache.cacheGenres(response.genres)
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }
}