package com.morse.movie.ui.home.viewmodel

import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.domain.usecase.LoadInComingMovies
import com.morse.movie.domain.usecase.LoadNowPlayingMovies
import com.morse.movie.domain.usecase.LoadPopularMovies
import com.morse.movie.domain.usecase.LoadTopRatedMovies
import com.morse.movie.ui.home.entities.HomeAction
import com.morse.movie.ui.home.entities.HomeResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeAnnotateProcessor(
    private val loadPopularMovies: LoadPopularMovies,
    private val loadTopRatedMovies: LoadTopRatedMovies,
    private val loadInComingMovies: LoadInComingMovies,
    private val loadNowPlayingMovies: LoadNowPlayingMovies
) {

    private val popularMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadPopularMovies, HomeResult> {
            it?.flatMap {

                loadPopularMovies?.execute()
                    ?.map {
                        HomeResult.Success(it?.results as ArrayList<Result>, 0) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn {
                        HomeResult.Error(it, 0)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(HomeResult.IsLoading(0))

            }
        }


    private val topRatedMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadTopRatedMovies, HomeResult> {
            it?.flatMap {
                loadTopRatedMovies?.execute()
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result>, 1) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn { HomeResult.Error(it, 1) }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.startWith(HomeResult.IsLoading(1))

            }
        }


    private val inComingMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadIncomingMovies, HomeResult> {
            it?.flatMap {
                loadInComingMovies?.execute()
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result>, 2) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn { HomeResult.Error(it, 2) }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.startWith(HomeResult.IsLoading(2))

            }
        }

    private val nowPlayingMoviesAnnotateProcessor =
        ObservableTransformer<HomeAction.LoadNowPlayingMovies, HomeResult> {
            it?.flatMap {
                loadNowPlayingMovies?.execute()
                    ?.map { HomeResult.Success(it?.results as ArrayList<Result>, 3) }
                    ?.cast(HomeResult::class.java)
                    ?.onErrorReturn { HomeResult.Error(it, 3) }
                    ?.observeOn(AndroidSchedulers.mainThread())
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
                    .compose(inComingMoviesAnnotateProcessor),
                it?.ofType(HomeAction.LoadNowPlayingMovies::class.java)
                    .compose(nowPlayingMoviesAnnotateProcessor)
            )
        }


    }

}