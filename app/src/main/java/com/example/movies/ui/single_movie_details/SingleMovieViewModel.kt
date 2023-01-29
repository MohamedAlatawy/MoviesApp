package com.example.movies.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movies.data.repository.NetworkState
import com.example.movies.data.vo.MoviesDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository : MovieDetailsRepository, movieId : Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MoviesDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}













