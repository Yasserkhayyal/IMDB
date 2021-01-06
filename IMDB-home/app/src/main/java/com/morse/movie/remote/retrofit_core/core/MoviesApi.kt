package com.morse.movie.remote.retrofit_core.core

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.entity.personresponse.PersonResponse
import com.morse.movie.utils.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

open interface MoviesApi {

    @Retry
    @GET(value = popularMovies + "&page=2")
    public fun getPopularMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = popularMovies)
    public fun getPopularMovieWithPagination (@Query ("page") page_id : Int) : Deferred<MovieResponse>

    @Retry
    @GET(value = topRatedMovies+ "&page=3")
    public fun getTopRatedMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = topRatedMovies)
    public fun getTopRatedMovieWithPagination (@Query ("page") page_id : Int) : Deferred<MovieResponse>

    @Retry
    @GET(value = inCommingMovies+ "&page=4")
    public fun getIncomingMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = inCommingMovies)
    public fun getIncomingMovieWithPagination (@Query ("page") page_id : Int) : Deferred<MovieResponse>

    @Retry
    @GET(value = nowplayingMovies+ "&page=1")
    public fun getNowPlayingMovie () : Deferred<MovieResponse>

    @Retry
    @GET(value = nowplayingMovies)
    public fun getNowPlayingMovieWithPagination (@Query ("page") page_id : Int) : Deferred<MovieResponse>

    @Retry
    @GET(value = similarMovies)
    public fun getSimilarMovies ( @Path("movie_id") movieId : Int ) : Deferred<MovieResponse>

    @Retry
    @GET(value = movieDetail)
    public fun getMovieDetail ( @Path("movie_id") movieId : Int ) : Deferred<MovieDetailResponse>

    @Retry
    @GET(value = movieReview)
    public fun getMovieReview (@Path("movie_id") movieId : Int) : Deferred<MovieReview>

    @Retry
    @GET(value = movieVideos)
    public fun getMovieVideos (@Path("movie_id") movieId : Int) : Deferred<MovieVideoResponse>

    @Retry
    @GET(value = searchMovies)
    public fun searchMovie(@Query ("page") page_id : Int ,
                           @Query ("query") query_text : String ) : Deferred<MovieResponse>

    @Retry
    @GET(value = personProfile)
    public fun getPersonProfile(@Path("person_id") personId : String) : Deferred<PersonResponse>

}