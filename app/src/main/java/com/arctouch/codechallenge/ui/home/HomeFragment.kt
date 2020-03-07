package com.arctouch.codechallenge.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val mViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.upcomingMovies.observe(this as LifecycleOwner, Observer {
            setUpAdapter(it)
            progressBar.visibility = View.GONE
        })
    }

    private fun setUpAdapter(movies: List<Movie>) {
        recyclerView.adapter = HomeAdapter(movies) { movie ->
            openDetails(movie)
        }
    }

    private fun openDetails(movie: Movie) {
        val direction =
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie)
        view?.let { view ->
            Navigation.findNavController(view).navigate(direction)
        }
    }
}