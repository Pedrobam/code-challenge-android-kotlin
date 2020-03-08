package com.arctouch.codechallenge.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomePagedFragment : Fragment() {

    private val mViewModel: HomePagedViewModel by viewModel()
    private val homePagedListAdapter = HomePagedListAdapter()
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_paged, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeLiveData()
        initializeList()
        mViewModel.filterTextAll.value = ""
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
                mViewModel.filterTextAll.value = newText
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    private fun observeLiveData() {
        mViewModel.getMovies().observe(this as LifecycleOwner, Observer {
            progressBar.visibility = View.GONE
            homePagedListAdapter.submitList(it)
        })
    }

    private fun initializeList() {
        recyclerView.adapter = homePagedListAdapter
    }
}