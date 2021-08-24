package com.arctouch.codechallenge.ui.home

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.arctouch.codechallenge.di.TmbRepository
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.ui.home.paging.MoviesDataSource


class HomeViewModel(private val repository: TmbRepository) : ViewModel() {

    private lateinit var postLiveData: LiveData<PagedList<Movie>>
    var filterTextAll = MutableLiveData<String>()

    init {
        initPaging()
    }

    private fun initPaging() {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()
        postLiveData = Transformations.switchMap(filterTextAll) { input ->
            initializedPagesListBuilder(config, input).build()
        }
    }

    fun getMovies(): LiveData<PagedList<Movie>> = postLiveData

    private fun initializedPagesListBuilder(
        config: PagedList.Config,
        input: String
    ): LivePagedListBuilder<Int, Movie> {
        val dataSourceFactory = object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return MoviesDataSource(
                    repository,
                    viewModelScope,
                    input
                )
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}