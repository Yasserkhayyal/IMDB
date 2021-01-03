package com.morse.movie.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.ui.detail.entities.DetailResult
import io.reactivex.Observable

interface DataRepository {

    //------------------------------Remote--------------------------

    public fun loadPopularMovies () : Observable<MovieResponse>

    public fun loadTopRatedMovies () : Observable<MovieResponse>

    public fun loadNowPlayingMovies () : Observable<MovieResponse>

    public fun loadNewComingMovies () : Observable<MovieResponse>

    public fun loadPopularMoviesByPagination() : Observable<LiveData<PagedList<Result>>>

    public fun loadTopRatedMoviesByPagination () : Observable<LiveData<PagedList<Result>>>

    public fun loadInComingMoviesByPagination () : Observable<LiveData<PagedList<Result>>>

    public fun loadNowPlayingMoviesByPagination () : Observable<LiveData<PagedList<Result>>>

    public fun searchOnMoviesByPagination (movieName : String) : Observable<LiveData<PagedList<Result>>>

    public fun loadMovieDetail (movieId : Int) : Observable<MovieDetailResponse>

    public fun loadSimilarMovies (movieId : Int) : Observable<MovieResponse>

    public fun loadReviewMovies (movieId : Int) : Observable<MovieReview>

    public fun loadVideoMovies (movieId : Int) : Observable<MovieVideoResponse>
    //------------------------------Local--------------------------

    public fun loadFavouriteMovies () : Observable<List<MovieDetailResponse>>

    public fun addMovieToFavourite (movie : MovieDetailResponse) : Observable<Boolean>

    public fun removeMovieFromFavourite (movieId : Int) : Observable<Boolean>

    public fun checkIfMovieExistInDataBase (movieId : Int) : Observable<Boolean>

}