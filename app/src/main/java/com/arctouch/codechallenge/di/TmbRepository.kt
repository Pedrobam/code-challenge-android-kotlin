package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

interface TmbRepository : TmdbApi

class TmbRepositoryImpl(private val api: TmdbApi) : TmbRepository {
    override suspend fun genres(apiKey: String, language: String): GenreResponse {
        return api.genres(apiKey, language)
    }

    override suspend fun upcomingMovies(
        apiKey: String,
        language: String,
        page: Long
    ): UpcomingMoviesResponse {
        return api.upcomingMovies(apiKey, language, page)
    }

    override fun movie(id: Long, apiKey: String, language: String): Observable<Movie> {
        return api.movie(id, apiKey, language)
    }

}