package com.morse.movie.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.cache.DSCacheManager
import com.morse.movie.data.cache.CacheInterface
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.domain.repository.DataRepository
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.entity.personresponse.PersonResponse
import com.morse.movie.data.local.LocalInterface
import com.morse.movie.data.remote.RemoteInterface
import io.reactivex.Observable

class DataRepositoryImpl(
    private val remoteInterface: RemoteInterface? = null,
    private val localeInterface: LocalInterface? = null,
    private val cacheInterface: CacheInterface? = DSCacheManager.newInstance()
) : DataRepository {

    override fun loadPopularMovies(): Observable<MovieResponse> {
        return if (cacheInterface?.isExistInPopularCache("2") == true) {
            Observable.just(cacheInterface?.getPopularMoviesFromCache("2"))
        } else {
            remoteInterface?.loadPopularMoviesFromRemoteSource()!!
        }
    }

    override fun loadTopRatedMovies(): Observable<MovieResponse> {
        return if (cacheInterface?.isExistInTopRatedCache("3") == true) Observable.just(
            cacheInterface?.getTopRatedMoviesFromCache("3")
        ) else remoteInterface?.loadTopRatedMoviesFromRemoteSource()!!
    }

    override fun loadNowPlayingMovies(): Observable<MovieResponse> {
        return if (cacheInterface?.isExistInNowPlayingCache("1") == true) Observable.just(
            cacheInterface?.getNowPlayingMoviesFromCache("1")
        ) else remoteInterface?.loadNowPlayingMoviesFromRemoteSource()!!
    }

    override fun loadNewComingMovies(): Observable<MovieResponse> {
        return if (cacheInterface?.isExistInInComingCache("4") == true) Observable.just(
            cacheInterface?.getInComingMoviesFromCache("4")
        ) else remoteInterface?.loadInComingMoviesFromRemoteSource()!!
    }

    override fun loadFavouriteMovies(): Observable<List<MovieDetailResponse>> {
        return localeInterface?.selectAllMoviesFromDataBase()!!
    }

    override fun addMovieToFavourite(movie: MovieDetailResponse): Observable<Boolean> {
        return localeInterface?.addMovieIntoDataBase(movie)!!
    }

    override fun removeMovieFromFavourite(movieId: Int): Observable<Boolean> {
        return localeInterface?.removeMovieIntoDataBase(movieId)!!
    }

    override fun removeAllMovieFromFavourite(): Observable<Boolean> {
        return localeInterface?.removeAllMoviesFromDatabase()!!
    }

    override fun loadPopularMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadPopularMoviesByPaginationFromRemoteSource()!!
    }

    override fun loadTopRatedMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadTopRatedMoviesByPaginationFromRemoteSource()!!
    }

    override fun loadInComingMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadInComingMoviesByPaginationFromRemoteSource()!!
    }

    override fun loadNowPlayingMoviesByPagination(): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.loadNowPlayingMoviesByPaginationFromRemoteSource()!!
    }

    override fun searchOnMoviesByPagination(movieName: String): Observable<LiveData<PagedList<Result>>> {
        return remoteInterface?.searchOnMoviesByPaginationFromRemoteSource(movieName)!!
    }

    override fun loadMovieDetail(movieId: Int): Observable<MovieDetailResponse> {
        return if (cacheInterface?.isExistInDetailCache("${movieId}") == true) Observable.just(
            cacheInterface?.getDetailMovieFromCache("${movieId}")
        ) else remoteInterface?.loadMovieDetailFromRemoteSource(movieId = movieId)!!
    }

    override fun loadSimilarMovies(movieId: Int): Observable<MovieResponse> {
        return if (cacheInterface?.isExistInSimilarCache("${movieId}") == true) Observable.just(
            cacheInterface?.getSimilarMoviesFromCache("${movieId}")
        ) else remoteInterface?.loadSimilarMoviesFromRemoteSource(movieId)!!
    }

    override fun loadReviewMovies(movieId: Int): Observable<MovieReview> {
        return if (cacheInterface?.isExistInReviewsCache("${movieId}") == true) Observable.just(
            cacheInterface?.getReviewsMovieFromCache("${movieId}")
        ) else remoteInterface?.loadReviewMoviesFromRemoteSource(movieId)!!
    }

    override fun loadVideoMovies(movieId: Int): Observable<MovieVideoResponse> {
        return if (cacheInterface?.isExistInVideosCache("${movieId}") == true) Observable.just(
            cacheInterface?.getVideosMovieFromCache("${movieId}")
        ) else remoteInterface?.loadVideoMoviesFromRemoteSource(movieId)!!
    }

    override fun checkIfMovieExistInDataBase(movieId: Int): Observable<Boolean> {
        return localeInterface?.checkIfMovieExistInDataBase(movieId)!!
    }

    override fun loadUserProfile(persionId: String): Observable<PersonResponse> {
        return remoteInterface?.loadUserProfileFromRemoteSource(persionId)!!
    }
}