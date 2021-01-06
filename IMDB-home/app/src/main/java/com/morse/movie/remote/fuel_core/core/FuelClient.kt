package com.morse.movie.remote.fuel_core.core

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.core.interceptors.loggingRequestInterceptor
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponseDeserializer
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.MovieResponseDeserializer
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.moviereviewresponse.MovieReviewResponseDeserializer
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponseDeserializer
import com.morse.movie.data.entity.personresponse.PersonProfileResponseDeserializer
import com.morse.movie.data.entity.personresponse.PersonResponse
import com.morse.movie.data.remote.RemoteInterface
import com.morse.movie.remote.base.DataSourceManager
import com.morse.movie.remote.retrofit_core.datasource.manager.FuelMoreDataSourceManager
import com.morse.movie.remote.retrofit_core.datasource.manager.RetrofitMoreDataSourceManager
import com.morse.movie.utils.*
import io.reactivex.Observable
import java.lang.Exception

class FuelClient (private var moreDataStoreManager : DataSourceManager) : RemoteInterface {

    init {
        FuelManager.instance.basePath = baseApi
        FuelManager.instance.addRequestInterceptor(loggingRequestInterceptor())
    }

    override fun loadPopularMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {
            Fuel.get(popularMovies)?.responseObject(MovieResponseDeserializer() , object : Handler<MovieResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadTopRatedMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {
            Fuel.get(topRatedMovies)?.responseObject(MovieResponseDeserializer() , object : Handler<MovieResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadInComingMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {
            Fuel.get(inCommingMovies)?.responseObject(MovieResponseDeserializer() , object : Handler<MovieResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadNowPlayingMoviesFromRemoteSource(): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {
            Fuel.get(nowplayingMovies)?.responseObject(MovieResponseDeserializer() , object : Handler<MovieResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadPopularMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(0)
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }   //Done

    override fun loadTopRatedMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(1)
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }  //Done

    override fun loadInComingMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(2)
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }  //Done

    override fun loadNowPlayingMoviesByPaginationFromRemoteSource(): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMovies(3)
                it?.onNext(result)
            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }  //Done

    override fun searchOnMoviesByPaginationFromRemoteSource(movieName: String): Observable<LiveData<PagedList<Result>>> {
        return Observable.create<LiveData<PagedList<Result>>> {
            try {
                var result = moreDataStoreManager?.getPaginationMoviesSearch(movieName)
                it?.onNext(result)
            }catch (e : Exception){
                it?.onError(e)
            }
        }
    }  //Done

    override fun loadMovieDetailFromRemoteSource(movieId: Int): Observable<MovieDetailResponse> {
        return Observable.create<MovieDetailResponse> {
            Fuel.get(movieDetail?.replace("{movie_id}" , "${movieId}"))?.responseObject(MovieDetailResponseDeserializer() , object : Handler<MovieDetailResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieDetailResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadSimilarMoviesFromRemoteSource(movieId: Int): Observable<MovieResponse> {
        return Observable.create<MovieResponse> {
            Fuel.get(similarMovies?.replace("{movie_id}" , "${movieId}"))?.responseObject(MovieResponseDeserializer() , object : Handler<MovieResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadReviewMoviesFromRemoteSource(movieId: Int): Observable<MovieReview> {
        return Observable.create<MovieReview> {
            Fuel.get(movieReview?.replace("{movie_id}" , "${movieId}"))?.responseObject(MovieReviewResponseDeserializer() , object : Handler<MovieReview>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieReview) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadVideoMoviesFromRemoteSource(movieId: Int): Observable<MovieVideoResponse> {
        return Observable.create<MovieVideoResponse> {
            Fuel.get(movieVideos?.replace("{movie_id}" , "${movieId}"))?.responseObject(MovieVideoResponseDeserializer() , object : Handler<MovieVideoResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: MovieVideoResponse) {
                    it?.onNext(value)
                }
            })
        }
    }  //Done

    override fun loadUserProfileFromRemoteSource(persionId: String): Observable<PersonResponse> {
        return Observable.create {
            Fuel.get(personProfile?.replace("{person_id}" , persionId))?.responseObject(PersonProfileResponseDeserializer() , object : Handler<PersonResponse>{
                override fun failure(error: FuelError) {
                    it?.onError(error?.exception)
                }

                override fun success(value: PersonResponse) {
                it?.onNext(value)
                }
            })
        }
    } //Done
}