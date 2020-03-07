package com.arctouch.codechallenge.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctouch.codechallenge.App
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val api = App.api

    fun getMovie(movieId: Long) {
        viewModelScope.launch {
            val movie = api.movie(movieId, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
            _movie.value = movie
        }
    }
}