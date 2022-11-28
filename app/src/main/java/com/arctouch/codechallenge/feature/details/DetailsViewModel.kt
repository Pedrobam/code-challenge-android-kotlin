package com.arctouch.codechallenge.feature.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctouch.codechallenge.di.TmbRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Error
import com.arctouch.codechallenge.util.UiState
import com.arctouch.codechallenge.util.Success
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: TmbRepository) : ViewModel() {

    private val _movie = MutableLiveData<UiState<Movie>>()
    val movie: LiveData<UiState<Movie>> = _movie

    fun getMovie(movieId: Long) {
        viewModelScope.launch {
            try {
                val movie = repository.movie(id = movieId)
                _movie.postValue(Success(movie))
            } catch (e: Exception) {
                _movie.postValue(Error(e.localizedMessage))
            }
        }
    }
}