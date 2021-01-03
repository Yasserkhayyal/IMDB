package com.morse.movie.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.domain.repository.DataRepository
import com.morse.movie.remote.retrofit_core.MoviesApi
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.local.LocalInterface
import com.morse.movie.data.remote.RemoteInterface
import com.morse.movie.ui.detail.entities.DetailResult
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DataRepositoryImpl(private val remoteInterface: RemoteInterface , private val localeInterface : LocalInterface) : DataRepository {

    override fun loadPopularMovies(): Observable<MovieResponse> {
        return remoteInterface?.loadPopularMoviesFromRemoteSource()
    }

    override fun loadTopRatedMovies(): Observable<MovieResponse> {
        return remoteInterface?.loadTopRatedMoviesFromRemoteSource()
    }

    override fun loadNowPlayingMovies(): Observable<MovieResponse> {
        return remoteInterface?.loadNowPlayingMoviesFromRemoteSource()
    }

    override fun loadNewComingMovies(): Observable<MovieResponse> {
        return remoteInterface?.loadInComingMoviesFromRemoteSource()
    }

    override fun loadFavouriteMovies(): Observable< List<MovieDetailResponse>> {
        return localeInterface?.selectAllMoviesFromDataBase()
    }

    override fun addMovieToFavourite( movie : MovieDetailResponse ): Observable<Boolean> {
        return  localeInterface?.addMovieIntoDataBase(movie)
    }

    override fun removeMovieFromFavourite(movieId : Int): Observable<Boolean> {
        return localeInterface?.removeMovieIntoDataBase(movieId)
    }

    override fun loadPopularMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadPopularMoviesByPaginationFromRemoteSource()
    }

    override fun loadTopRatedMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadTopRatedMoviesByPaginationFromRemoteSource()
    }

    override fun loadInComingMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadInComingMoviesByPaginationFromRemoteSource()
    }

    override fun loadNowPlayingMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadNowPlayingMoviesByPaginationFromRemoteSource()
    }

    override fun searchOnMoviesByPagination(movieName: String): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.searchOnMoviesByPaginationFromRemoteSource(movieName)
    }

    override fun loadMovieDetail(movieId: Int): Observable<MovieDetailResponse> {
        return remoteInterface?.loadMovieDetailFromRemoteSource(movieId = movieId)
    }

    override fun loadSimilarMovies(movieId: Int): Observable<MovieResponse> {
        return remoteInterface?.loadSimilarMoviesFromRemoteSource(movieId)
    }

    override fun loadReviewMovies(movieId: Int): Observable<MovieReview> {
        return remoteInterface?.loadReviewMoviesFromRemoteSource(movieId)
    }

    override fun loadVideoMovies(movieId: Int): Observable<MovieVideoResponse> {
        return remoteInterface?.loadVideoMoviesFromRemoteSource(movieId)
    }

    override fun checkIfMovieExistInDataBase(movieId: Int): Observable<Boolean> {
        return localeInterface?.checkIfMovieExistInDataBase(movieId)
    }
}