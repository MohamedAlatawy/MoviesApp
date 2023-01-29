package com.example.movies.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.api.TheMovieDBInterface
import com.example.movies.data.vo.MoviesDetails
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io

class MovieDetailsNetworkDataSource(private val apiService : TheMovieDBInterface,
                                    private val compositeDisposable: CompositeDisposable) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
    get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MoviesDetails>()
    val downloadedMovieResponse: LiveData<MoviesDetails>
    get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieID: Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieID)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)

                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDAtaSource", it.message.toString())
                        }
                    )
            )

        }catch (e: Exception){
            Log.e("MovieDetailsDAtaSource", e.message.toString())

        }
    }

}