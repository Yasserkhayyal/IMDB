package com.morse.movie.remote.retrofit_core.core

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.cache.DSCacheManager
import com.morse.movie.data.cache.CacheInterface
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.entity.personresponse.PersonResponse
import com.morse.movie.data.remote.RemoteInterface
import com.morse.movie.remote.base.DataSourceManager
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RetrofitClient(
    private var moreDataStoreManager: DataSourceManager? = null,
    private val cacheInterface: CacheInterface?= DSCacheManager.newInstance()
) : RemoteInterface {

    private var retrofitMoviesApi = RetrofitBuilder.getNetworkInteractor()

    override fun loadPopularMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {

            CoroutineScope(Dispatchers.IO)?.launch {
                try {
                    var response =
                        retrofitMoviesApi.getPopularMovie()?.await()
                    cacheInterface?.addNewPopularMovieIntoCache("2", response)
                    it?.onNext(response)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadTopRatedMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {

            CoroutineScope(Dispatchers.IO)?.launch {
                try {
                    var response =
                        retrofitMoviesApi.getTopRatedMovie()?.await()
                    cacheInterface?.addNewTopRatedMovieIntoCache("3", response)
                    it?.onNext(response)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadInComingMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {

            CoroutineScope(Dispatchers.IO)?.launch {
                try {
                    var response =
                        retrofitMoviesApi.getIncomingMovie()?.await()
                    cacheInterface?.addNewInComingMovieIntoCache("4", response)
                    it?.onNext(response)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadNowPlayingMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {

            CoroutineScope(Dispatchers.IO)?.launch {
                try {
                    var response =
                        retrofitMoviesApi.getNowPlayingMovie()?.await()
                    cacheInterface?.addNewNowPlayingMovieIntoCache("1", response)
                    it?.onNext(response)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadPopularMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(0)!!
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }

    override fun loadTopRatedMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(0)!!
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }

    override fun loadInComingMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(0)!!
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }

    override fun loadNowPlayingMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(0)!!
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }

    override fun searchOnMoviesByPaginationFromRemoteSource(movieName: String): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMoviesSearch(movieName)!!
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }

    override fun loadMovieDetailFromRemoteSource(movieId: Int): Observable<MovieDetailResponse> {
        return Observable.create<MovieDetailResponse> {

            CoroutineScope(Dispatchers.IO)?.launch {
                try {
                    var response =
                        retrofitMoviesApi.getMovieDetail(movieId)?.await()
                    cacheInterface?.addNewMovieDetailIntoCache("${movieId}", response)
                    it?.onNext(response)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadSimilarMoviesFromRemoteSource(movieId: Int): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var result = retrofitMoviesApi
                        .getSimilarMovies(movieId)?.await()
                    cacheInterface?.addNewSimilarMovieIntoCache("${movieId}", result)
                    it?.onNext(result)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadReviewMoviesFromRemoteSource(movieId: Int): Observable<MovieReview> {
        return Observable.create<MovieReview> {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var result = retrofitMoviesApi
                        .getMovieReview(movieId)?.await()
                    cacheInterface?.addNewReviewsIntoCache("${movieId}", result)
                    it?.onNext(result)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadVideoMoviesFromRemoteSource(movieId: Int): Observable<MovieVideoResponse> {
        return Observable.create<MovieVideoResponse> {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var result = retrofitMoviesApi
                        .getMovieVideos(movieId)?.await()
                    cacheInterface?.addNewVideoIntoCache("${movieId}", result)
                    it?.onNext(result)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }

    override fun loadUserProfileFromRemoteSource(persionId: String): Observable<PersonResponse> {
        return Observable.create<PersonResponse> {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var result = retrofitMoviesApi
                        .getPersonProfile(persionId)?.await()
                    it?.onNext(result)
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }

        }
    }
}