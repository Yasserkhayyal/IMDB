package com.morse.movie.data.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.entity.personresponse.PersonResponse
import io.reactivex.Observable

interface RemoteInterface {

    public fun loadPopularMoviesFromRemoteSource () : Observable<MovieResponse>

    public fun loadTopRatedMoviesFromRemoteSource () : Observable<MovieResponse>

    public fun loadInComingMoviesFromRemoteSource () : Observable<MovieResponse>

    public fun loadNowPlayingMoviesFromRemoteSource () : Observable<MovieResponse>

    public fun loadPopularMoviesByPaginationFromRemoteSource () : Observable<LiveData<PagedList<Result>>>

    public fun loadTopRatedMoviesByPaginationFromRemoteSource () : Observable<LiveData<PagedList<Result>>>

    public fun loadInComingMoviesByPaginationFromRemoteSource () : Observable<LiveData<PagedList<Result>>>

    public fun loadNowPlayingMoviesByPaginationFromRemoteSource () : Observable<LiveData<PagedList<Result>>>

    public fun searchOnMoviesByPaginationFromRemoteSource (movieName : String) : Observable<LiveData<PagedList<Result>>>

    public fun loadMovieDetailFromRemoteSource (movieId : Int) : Observable<MovieDetailResponse>

    public fun loadSimilarMoviesFromRemoteSource (movieId : Int) : Observable<MovieResponse>

    public fun loadReviewMoviesFromRemoteSource (movieId : Int) : Observable<MovieReview>

    public fun loadVideoMoviesFromRemoteSource (movieId : Int) : Observable<MovieVideoResponse>

    public fun loadUserProfileFromRemoteSource (persionId : String) : Observable<PersonResponse>

}