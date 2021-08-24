package com.arctouch.codechallenge.ui.home.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.di.TmbRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MoviesDataSource(
    private val repository: TmbRepository,
    private val scope: CoroutineScope,
    private val input: String? = null
) :
    PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        scope.launch {
            try {
                val upcomingMoviesResponse = if (input.isNullOrEmpty()) {
                    repository.upcomingMovies(page = 1)
                } else {
                    repository.getMoviesByName(input, page = 1)
                }
                val moviesWithGenres = addGenres(upcomingMoviesResponse)
                callback.onResult(moviesWithGenres, null, 2)
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                val page = params.key
                val upcomingMoviesResponse = if (input.isNullOrEmpty()) {
                    repository.upcomingMovies(page = page.toLong())
                } else {
                    repository.getMoviesByName(input, page = page.toLong())
                }
                val moviesWithGenres = addGenres(upcomingMoviesResponse)
                callback.onResult(moviesWithGenres, page.plus(1))
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("Not yet implemented")
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }

    private fun addGenres(upcomingMoviesResponse: UpcomingMoviesResponse): List<Movie> {
        return upcomingMoviesResponse.results.map { movie ->
            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
        }
    }
}