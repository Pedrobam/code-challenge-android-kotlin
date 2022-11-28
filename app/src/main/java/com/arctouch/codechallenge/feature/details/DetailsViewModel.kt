package com.arctouch.codechallenge.feature.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arctouch.codechallenge.di.TmbRepository
import com.arctouch.codechallenge.model.Movie
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: TmbRepository) : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    fun getMovie(movieId: Long) {
        viewModelScope.launch {
            try {
                val movie = repository.movie(id = movieId)
                _movie.postValue(movie)
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }
}