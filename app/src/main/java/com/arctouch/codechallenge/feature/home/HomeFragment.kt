package com.arctouch.codechallenge.feature.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.databinding.FragmentHomePagedBinding
import com.arctouch.codechallenge.model.Movie
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), MenuProvider {

    private val mViewModel: HomeViewModel by viewModel()
    private lateinit var homePagedListAdapter: HomeAdapter
    private lateinit var searchView: SearchView
    private lateinit var binding: FragmentHomePagedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePagedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        observeLiveData()
        initializeList()
        setFilterText()
    }

    private fun setFilterText() {
        val value = mViewModel.filterTextAll.value
        if (value == null) {
            mViewModel.filterTextAll.value = ""
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchAction = menu.findItem(R.id.search_action)
        searchView = searchAction?.actionView as SearchView
        configSearchView()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
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
            binding.progressBar.visibility = View.GONE
            homePagedListAdapter.submitList(it)
        })
    }

    private fun openDetails(movie: Movie) {
        val direction = HomeFragmentDirections.actionHomePagedFragmentToDetailsFragment(movie.id)
        view?.let {
            Navigation.findNavController(it).navigate(direction)
        }
    }

    private fun initializeList() {
        homePagedListAdapter = HomeAdapter { movie ->
            openDetails(movie)
        }
        binding.recyclerView.adapter = homePagedListAdapter
    }
}