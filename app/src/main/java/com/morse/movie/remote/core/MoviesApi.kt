package com.morse.movie.remote.core

import com.morse.movie.remote.entity.MovieResponse
import com.morse.movie.utils.inCommingMovies
import com.morse.movie.utils.nowplayingMovies
import com.morse.movie.utils.popularMovies
import com.morse.movie.utils.topRatedMovies
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

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

}