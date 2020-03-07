package com.arctouch.codechallenge.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctouch.codechallenge.App
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val api = App.api
    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies

    init {
        getUpComingMovies()
    }

    fun getUpComingMovies() {
        viewModelScope.launch {
            val upcomingMoviesResponse = api.upcomingMovies(
                TmdbApi.API_KEY,
                TmdbApi.DEFAULT_LANGUAGE,
                1,
                TmdbApi.DEFAULT_REGION
            )
            val moviesWithGenres = upcomingMoviesResponse.results.map { movie ->
                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
            }
            _upcomingMovies.value = moviesWithGenres
        }
    }
}