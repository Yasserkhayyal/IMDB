package com.morse.movie.ui.detail.viewmodel

import com.morse.movie.remote.core.RetrofitBuilder
import com.morse.movie.remote.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.remote.entity.movieresponse.MovieResponse
import com.morse.movie.ui.detail.entities.DetailAction
import com.morse.movie.ui.detail.entities.DetailResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailAnnotateProcessor {

    private val loadMovieDetailAnnotateProcessor =
        ObservableTransformer<DetailAction.LoadMovieDetails, DetailResult.MovieDetailResult> {
            it?.flatMap {
                var ourAction = it
                return@flatMap Observable.create<MovieDetailResponse> {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var result = RetrofitBuilder.getNetworkInteractor()
                                .getMovieDetail(ourAction?.movieId)?.await()
                            it?.onNext(result)
                        } catch (e: Exception) {
                            it?.onError(e)
                        }
                    }

                }
                    ?.map {
                        DetailResult.MovieDetailResult.Success(it)
                    }?.cast(DetailResult.MovieDetailResult::class.java)
                    ?.onErrorReturn {
                        DetailResult.MovieDetailResult.Error(it)
                    }?.startWith(DetailResult.MovieDetailResult.Loading)
                    ?.subscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(Schedulers.computation())
            }
        }

    private val loadSimilarMoviesAnnotateProcessor =
        ObservableTransformer<DetailAction.LoadSimilarMovies, DetailResult.MovieSimilarsResult> {
            it?.flatMap {
                var ourAction = it
                return@flatMap Observable.create<MovieResponse> {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var result = RetrofitBuilder.getNetworkInteractor()
                                .getSimilarMovies(ourAction?.movieId)?.await()
                            it?.onNext(result)
                        } catch (e: Exception) {
                            it?.onError(e)
                        }
                    }

                }
                    ?.map {
                        DetailResult.MovieSimilarsResult.Success(it)
                    }?.cast(DetailResult.MovieSimilarsResult::class.java)
                    ?.onErrorReturn {
                        DetailResult.MovieSimilarsResult.Error(it)
                    }?.startWith(DetailResult.MovieSimilarsResult.Loading)
                    ?.subscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(Schedulers.computation())
            }
        }

    public val detailAnnotateProcesser =  ObservableTransformer<DetailAction, DetailResult>{
        it?.publish {
            Observable.merge(
                it?.ofType(DetailAction.LoadMovieDetails::class.java).compose(loadMovieDetailAnnotateProcessor) ,
                it?.ofType(DetailAction.LoadSimilarMovies::class.java).compose(loadSimilarMoviesAnnotateProcessor)
            )
        }
    }


}