package com.arctouch.codechallenge.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.databinding.FragmentDetailsBinding
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Error
import com.arctouch.codechallenge.util.Loading
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.Success
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val mViewModel: DetailsViewModel by viewModel()
    private val movieImageUrlBuilder by lazy { MovieImageUrlBuilder() }
    private lateinit var images: Array<String>
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieByArguments()
        observeMovie()
    }

    private fun getMovieByArguments() {
        arguments?.let {
            val safeArgs = DetailsFragmentArgs.fromBundle(it)
//            setActionBarTitle(safeArgs.movie.title)
            mViewModel.getMovie(safeArgs.movieId)
        }
    }

    private fun setActionBarTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    private fun observeMovie() {
        mViewModel.movie.observe(this as LifecycleOwner) {
            when (it) {
                is Loading -> {
                    //NOPY
                }
                is Success -> {
                    configMovie(it.data)
                    configCarousel(it.data)
                }
                is Error -> {
                    Snackbar.make(binding.root, it.message ?: requireContext().getString(R.string.network_generic_error), Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun configMovie(movie: Movie) {
        binding.apply {
            progressBarMovie.visibility = View.GONE
            content.visibility = View.VISIBLE

            tvTitle.text = movie.title
            tvGenres.text = context?.getString(
                R.string.movie_genres,
                movie.genres?.joinToString(separator = ", ") { it.name })
            tvReleaseDate.text = context?.getString(
                R.string.release_date, movie.releaseDate
            )
            tvOverview.text = movie.overview
        }
    }

    private fun configCarousel(movie: Movie) {
        images = arrayOf(
            movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath!!),
            movieImageUrlBuilder.buildPosterUrl(movie.posterPath!!)
        )

//        binding.carouselView.setImageListener { position, imageView ->
//            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
//            imageView.setBackgroundColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.colorPrimaryDark
//                )
//            )
//            Glide.with(imageView)
//                .load(images[position])
//                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
//                .into(imageView)
//
//        }
//        binding.carouselView.pageCount = images.size
    }
}