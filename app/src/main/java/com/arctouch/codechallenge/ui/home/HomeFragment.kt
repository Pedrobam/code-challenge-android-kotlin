package com.arctouch.codechallenge.ui.home

import android.app.SearchManager
import android.content.Context
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val mViewModel: HomeViewModel by viewModel()
    private var isLoading = false
    private var isSearching = false
    private lateinit var adapter: HomeAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setUpAdapter(mViewModel.upcomingMovies.value)
        configScrollListener(recyclerView)
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchAction = menu.findItem(R.id.search_action)
        searchView = searchAction?.actionView as SearchView
        configSearchView()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun configSearchView() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    setUpAdapter(mViewModel.upcomingMovies.value)
                    mViewModel.getUpComingMovies()
                    isSearching = false
                } else {
                    isSearching = true
                    mViewModel.searchMovies(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    private fun observeViewModel() {
        mViewModel.upcomingMovies.observe(this as LifecycleOwner, Observer {
            isLoading = false
            progressBar.visibility = View.GONE
            adapter.notifyDataSetChanged()
        })
        mViewModel.searchLiveData.observe(this as LifecycleOwner, Observer {
            setUpAdapter(it.toMutableList())
        })
    }

    private fun loadMoreMovies() {
        isLoading = true
        mViewModel.getUpComingMovies()
    }

    private fun setUpAdapter(movies: MutableList<Movie>?) {
        if (movies == null) return
        adapter = HomeAdapter(movies) { movie ->
            openDetails(movie)
        }
        recyclerView.adapter = adapter
    }

    private fun openDetails(movie: Movie) {
        val direction =
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie)
        view?.let { view ->
            Navigation.findNavController(view).navigate(direction)
        }
    }

    private fun configScrollListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isSearching) {
                    return
                }
                val layoutManager = recyclerView
                    .layoutManager as LinearLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE
                    ) {
                        loadMoreMovies()
                    }
                }
            }
        })
    }
}