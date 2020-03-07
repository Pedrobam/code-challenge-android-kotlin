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
    private val _upcomingMovies = MutableLiveData<MutableList<Movie>>()
    val upcomingMovies: LiveData<MutableList<Movie>> = _upcomingMovies
    private var page: Long = 0

    init {
        _upcomingMovies.value = mutableListOf()
        getUpComingMovies()
    }

    fun getUpComingMovies() {
        page = page.plus(1)
        viewModelScope.launch {
            val upcomingMoviesResponse = api.upcomingMovies(
                TmdbApi.API_KEY,
                TmdbApi.DEFAULT_LANGUAGE,
                page
            )
            val moviesWithGenres = upcomingMoviesResponse.results.map { movie ->
                movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
            }
            val teste = _upcomingMovies.value
            teste?.addAll(moviesWithGenres)
            _upcomingMovies.postValue(teste)
        }
    }
}