package com.arctouch.codechallenge.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctouch.codechallenge.App
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.di.TmbRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel(private val repository: TmbRepository) : ViewModel(), KoinComponent {

    private val _upcomingMovies = MutableLiveData<MutableList<Movie>>()
    val upcomingMovies: LiveData<MutableList<Movie>> = _upcomingMovies
    private val _searchLiveData = MutableLiveData<MutableList<Movie>>()
    var searchLiveData: LiveData<MutableList<Movie>> = _searchLiveData

    private var page: Long = 0

    init {
        _upcomingMovies.value = mutableListOf()
        getUpComingMovies()
    }

    fun getUpComingMovies() {
        page = page.plus(1)
        viewModelScope.launch {
            val upcomingMoviesResponse = repository.upcomingMovies(page = page)
            val moviesWithGenres = addGenres(upcomingMoviesResponse)
            val list = _upcomingMovies.value
            list?.addAll(moviesWithGenres)
            _upcomingMovies.postValue(list)
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val upcomingMoviesResponse = repository.getMoviesByName(query)
            val moviesWithGenres = addGenres(upcomingMoviesResponse)
            _searchLiveData.postValue(moviesWithGenres.toMutableList())
        }
    }

    private fun addGenres(upcomingMoviesResponse: UpcomingMoviesResponse): List<Movie> {
        return upcomingMoviesResponse.results.map { movie ->
            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
        }
    }
}