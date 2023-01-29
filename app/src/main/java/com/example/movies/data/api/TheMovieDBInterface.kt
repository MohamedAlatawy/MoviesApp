package com.example.movies.data.api

import com.example.movies.data.vo.MovieResponse
import com.example.movies.data.vo.MoviesDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page : Int) : Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id: Int) : Single<MoviesDetails>

}