package com.arctouch.codechallenge.api

import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    companion object {
        const val URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "1f54bd990f1cdfb230adb312546d765d"
        const val DEFAULT_LANGUAGE = "pt-BR"
        const val DEFAULT_REGION = "BR"
    }

    @GET("genre/movie/list")
    suspend fun genres(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): GenreResponse

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Long
    ): UpcomingMoviesResponse

    @GET("movie/{id}")
    suspend fun movie(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): Movie

    @GET("search/movie")
    suspend fun getMoviesByName(
        @Query("query") title: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Long
    ): UpcomingMoviesResponse
}
