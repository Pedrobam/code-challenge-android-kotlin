package com.arctouch.codechallenge.ui.home

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
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
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        configScrollListener(recyclerView)
        observeViewModel()
    }

    private fun observeViewModel() {
        mViewModel.upcomingMovies.observe(this as LifecycleOwner, Observer {
            isLoading = false
            progressBar.visibility = View.GONE
            adapter.list = it.toMutableList()
        })
    }

    private fun loadMoreMovies() {
        isLoading = true
        mViewModel.getUpComingMovies()
    }

    private fun setUpAdapter() {
        adapter = HomeAdapter { movie ->
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