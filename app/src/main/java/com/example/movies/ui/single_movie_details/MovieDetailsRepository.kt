package com.example.movies.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movies.data.api.TheMovieDBInterface
import com.example.movies.data.repository.MovieDetailsNetworkDataSource
import com.example.movies.data.repository.NetworkState
import com.example.movies.data.vo.MoviesDetails
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.CompletionService

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MoviesDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}