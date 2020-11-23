package com.morse.movie.ui.home.viewmodel

import com.morse.movie.remote.core.RetrofitBuilder
import com.morse.movie.remote.entity.movieresponse.MovieResponse
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movie.ui.home.entities.HomeAction
import com.morse.movie.ui.home.entities.HomeResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeAnnotateProcessor {

    private val popularMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadPopularMovies, HomeResult> {
            it?.flatMap {
                Observable.create<MovieResponse> {

                    CoroutineScope(Dispatchers.IO)?.launch {
                        try {
                            var response =
                                RetrofitBuilder.getNetworkInteractor().getPopularMovie()?.await()
                            it?.onNext(response)
                        }catch (e : Exception){
                            it?.onError(e)
                        }
                    }

                }
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result> , 0 ) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn {
                        HomeResult.Error(it , 0)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(HomeResult.IsLoading(0))

            }
        }


    private val topRatedMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadTopRatedMovies, HomeResult> {
            it?.flatMap {
                Observable.create<MovieResponse> {

                    CoroutineScope(Dispatchers.IO)?.launch {
                        try {
                            var response =
                                RetrofitBuilder.getNetworkInteractor().getTopRatedMovie()?.await()
                            it?.onNext(response)
                        }catch (e : Exception){
                            it?.onError(e)
                        }
                    }

                }
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result>, 1) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn { HomeResult.Error(it , 1) }
                    ?.observeOn( AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.startWith(HomeResult.IsLoading(1))

            }
        }


    private val inComingMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadIncomingMovies, HomeResult> {
            it?.flatMap {
                Observable.create<MovieResponse> {

                    CoroutineScope(Dispatchers.IO)?.launch {
                        try {
                            var response =
                                RetrofitBuilder.getNetworkInteractor().getIncomingMovie()?.await()
                            it?.onNext(response)
                        }catch (e : Exception){
                            it?.onError(e)
                        }
                    }

                }
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result> , 2) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn { HomeResult.Error(it ,2) }
                    ?.observeOn( AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.startWith(HomeResult.IsLoading(2))

            }
        }

    private val nowPlayingMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadNowPlayingMovies, HomeResult> {
            it?.flatMap {
                Observable.create<MovieResponse> {

                    CoroutineScope(Dispatchers.IO)?.launch {
                        try {
                            var response =
                                RetrofitBuilder.getNetworkInteractor().getNowPlayingMovie()?.await()
                            it?.onNext(response)
                        }catch (e : Exception){
                            it?.onError(e)
                        }
                    }

                }
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result> , 3) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn { HomeResult.Error(it ,3) }
                    ?.observeOn( AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.startWith(HomeResult.IsLoading(3))

            }
        }

    public val homeViewModelAnnotateProcessor = ObservableTransformer<HomeAction, HomeResult> {
        it?.publish {
            Observable.merge(
                it?.ofType(HomeAction.LoadPopularMovies::class.java)
                    .compose(popularMoviesAnnotateProcessor),
                it?.ofType(HomeAction.LoadTopRatedMovies::class.java)
                    .compose(topRatedMoviesAnnotateProcessor),
                it?.ofType(HomeAction.LoadIncomingMovies::class.java)
                    .compose(inComingMoviesAnnotateProcessor) ,
                it?.ofType(HomeAction.LoadNowPlayingMovies::class.java)
                    .compose(nowPlayingMoviesAnnotateProcessor)
            )
        }


    }

}