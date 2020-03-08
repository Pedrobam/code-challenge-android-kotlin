package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse

interface TmbRepository : TmdbApi

class TmbRepositoryImpl(private val api: TmdbApi) : TmbRepository {
    override suspend fun genres(apiKey: String, language: String): GenreResponse {
        return api.genres()
    }

    override suspend fun upcomingMovies(
        apiKey: String,
        language: String,
        page: Long
    ): UpcomingMoviesResponse {
        return api.upcomingMovies(page = page)
    }

    override suspend fun movie(id: Long, apiKey: String, language: String): Movie {
        return api.movie(id)
    }

    override suspend fun getMoviesByName(
        title: String,
        apiKey: String,
        language: String
    ): UpcomingMoviesResponse {
        return api.getMoviesByName(title)
    }

}