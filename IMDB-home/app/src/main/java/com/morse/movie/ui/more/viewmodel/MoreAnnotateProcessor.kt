package com.morse.movie.ui.more.viewmodel

import com.morse.movie.domain.usecase.LoadPaginationMovies
import com.morse.movie.domain.usecase.SearchOnMoviesUseCase
import com.morse.movie.ui.more.entities.MoreAction
import com.morse.movie.ui.more.entities.MoreResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MoreAnnotateProcessor (private val loadMoreUseCase: LoadPaginationMovies ,
                             private val searchOnMoviesUseCase: SearchOnMoviesUseCase) {

    private val morePopularMoviesAnnotateProcessor = ObservableTransformer<MoreAction.LoadPopularMoviesByPagination , MoreResult >{
        it?.flatMap {
            loadMoreUseCase?.execute(0)
                ?.map {
                MoreResult.Success(it)
            }?.cast(MoreResult::class.java)
                ?.onErrorReturn {
                    MoreResult.Error(it)
                }?.observeOn(AndroidSchedulers.mainThread())?.
                subscribeOn(Schedulers.computation())?.
                startWith(MoreResult.Loading)
        }
    }

    private val moreTopRatedMoviesAnnotateProcessor = ObservableTransformer<MoreAction.LoadTopRatedMoviesByPagination , MoreResult >{
        it?.flatMap {
            loadMoreUseCase?.execute(1)
                ?.map {
                MoreResult.Success(it)
            }?.cast(MoreResult::class.java)
                ?.onErrorReturn {
                    MoreResult.Error(it)
                }?.observeOn(AndroidSchedulers.mainThread())?.
                subscribeOn(Schedulers.computation())?.
                startWith(MoreResult.Loading)
        }
    }

    private val moreInComingMoviesAnnotateProcessor = ObservableTransformer<MoreAction.LoadInComingMoviesByPagination , MoreResult >{
        it?.flatMap {
            loadMoreUseCase?.execute(2)
                ?.map {
                MoreResult.Success(it)
            }?.cast(MoreResult::class.java)
                ?.onErrorReturn {
                    MoreResult.Error(it)
                }?.observeOn(AndroidSchedulers.mainThread())?.
                subscribeOn(Schedulers.computation())?.
                startWith(MoreResult.Loading)
        }
    }

    private val moreNowPlayingMoviesAnnotateProcessor = ObservableTransformer<MoreAction.LoadNowPlayingMoviesByPagination , MoreResult >{
        it?.flatMap {
            loadMoreUseCase?.execute(3)?.map {
                MoreResult.Success(it)
            }?.cast(MoreResult::class.java)
                ?.onErrorReturn {
                    MoreResult.Error(it)
                }?.observeOn(AndroidSchedulers.mainThread())?.
                subscribeOn(Schedulers.computation())?.
                startWith(MoreResult.Loading)
        }
    }

    private val searchOnMoviesAnnotateProcessor = ObservableTransformer<MoreAction.SearchOnMoviesByPaginationAction , MoreResult >{
        it?.flatMap {
            searchOnMoviesUseCase?.execute(it?.searchText)?.map {
                MoreResult.Success(it)
            }?.cast(MoreResult::class.java)
                ?.onErrorReturn {
                    MoreResult.Error(it)
                }?.observeOn(AndroidSchedulers.mainThread())?.
                subscribeOn(Schedulers.computation())?.
                startWith(MoreResult.Loading)
        }
    }

    public val moreAnnotateProcessor = ObservableTransformer<MoreAction , MoreResult >{
        it?.publish {
            Observable.merge(
                it?.ofType(MoreAction.LoadTopRatedMoviesByPagination::class.java).compose(moreTopRatedMoviesAnnotateProcessor) ,
                it?.ofType(MoreAction.LoadInComingMoviesByPagination::class.java).compose(moreInComingMoviesAnnotateProcessor) ,
                it?.ofType(MoreAction.LoadNowPlayingMoviesByPagination::class.java).compose(moreNowPlayingMoviesAnnotateProcessor) ,
                it?.ofType(MoreAction.LoadPopularMoviesByPagination::class.java).compose(morePopularMoviesAnnotateProcessor)
            )?.mergeWith(
                it?.ofType(MoreAction.SearchOnMoviesByPaginationAction::class.java).compose(searchOnMoviesAnnotateProcessor)
            )
        }
    }

}