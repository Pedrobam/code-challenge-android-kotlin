package com.arctouch.codechallenge.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private lateinit var mViewModel: DetailsViewModel
    private val movieImageUrlBuilder by lazy { MovieImageUrlBuilder() }
    private lateinit var images: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieByArguments()
        observeMovie()
    }

    private fun getMovieByArguments() {
        arguments?.let {
            val safeArgs = DetailsFragmentArgs.fromBundle(it)
            (activity as AppCompatActivity).supportActionBar?.title = safeArgs.movie.title
            mViewModel.getMovie(safeArgs.movie.id.toLong())
        }
    }

    private fun observeMovie() {
        mViewModel.movie.observe(this as LifecycleOwner, Observer { movie ->
            configMovie(movie)
            configCarousel(movie)
        })
    }

    private fun configMovie(movie: Movie) {
        progressBarMovie.visibility = View.GONE
        content.visibility = View.VISIBLE

        tv_title.text = movie.title
        tv_genres.text = context?.getString(
            R.string.movie_genres,
            movie.genres?.joinToString(separator = ", ") { it.name })
        tv_release_date.text = context?.getString(
            R.string.release_date, movie.releaseDate
        )
        tv_overview.text = movie.overview
    }

    fun configCarousel(movie: Movie) {
        images = arrayOf(
            movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath!!),
            movieImageUrlBuilder.buildPosterUrl(movie.posterPath!!)
        )

        carousel_view.setImageListener { position, imageView ->
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setBackgroundColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.colorPrimaryDark
                )
            )
            Glide.with(imageView)
                .load(images[position])
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(imageView)

        }
        carousel_view.pageCount = images.size
    }
}