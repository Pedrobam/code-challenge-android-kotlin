package com.arctouch.codechallenge.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.R
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val mViewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val safeArgs = DetailsFragmentArgs.fromBundle(it)
            mViewModel.getMovie(safeArgs.movie.id.toLong())
        }
        mViewModel.movie.observe(this as LifecycleOwner, Observer {
            progressBarMovie.visibility = View.GONE
            content.visibility = View.VISIBLE

            tv_title.text = it.title

            tv_release_date.text = it.releaseDate
            tv_overview.text = it.overview
        })
    }
}