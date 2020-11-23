package com.morse.movie.remote.core

import com.morse.movie.remote.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.remote.entity.movieresponse.MovieResponse
import com.morse.movie.utils.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

open interface MoviesApi {

    @Retry
    @GET(value = popularMovies)
    public fun getPopularMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = topRatedMovies)
    public fun getTopRatedMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = inCommingMovies)
    public fun getIncomingMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = nowplayingMovies)
    public fun getNowPlayingMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = similarMovies)
    public fun getSimilarMovies ( @Path("movie_id") movieId : Int ) : Deferred<MovieResponse>

    @Retry
    @GET(value = movieDetail)
    public fun getMovieDetail ( @Path("movie_id") movieId : Int ) : Deferred<MovieDetailResponse>

}