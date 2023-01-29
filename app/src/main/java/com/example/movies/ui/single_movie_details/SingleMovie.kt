package com.example.movies.ui.single_movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.data.api.POSTER_BASE_URL
import com.example.movies.data.api.TheMovieDBClient
import com.example.movies.data.api.TheMovieDBInterface
import com.example.movies.data.repository.NetworkState
import com.example.movies.data.vo.MoviesDetails
import com.example.movies.databinding.ActivitySingleMovieBinding
import java.text.NumberFormat
import java.util.*


@Suppress("DEPRECATION")
class SingleMovie() : AppCompatActivity(), LifecycleOwner {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    private lateinit var binding: ActivitySingleMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer{
            binding.progressBar.visibility = if ( it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if ( it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    fun bindUI(it: MoviesDetails) {
        binding.apply {

            movieTitle.text = it.title
            movieTagline.text = it.tagline
            movieReleaseDate.text = it.releaseDate
            movieRating.text = it.rating.toString()
            movieRuntime.text = it.runtime.toString() + " minutes"
            movieOverview.text = it.overview

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            movieBudget.text = formatCurrency.format(it.budget)
            movieRevenue.text = formatCurrency.format(it.revenue)

            val moviePosterURL = POSTER_BASE_URL + it.posterPath
            Glide.with(this@SingleMovie)
                .load(moviePosterURL)
                .into(ivMoviePoster)

        }
        }

    private fun getViewModel(movieId: Int) : SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

}