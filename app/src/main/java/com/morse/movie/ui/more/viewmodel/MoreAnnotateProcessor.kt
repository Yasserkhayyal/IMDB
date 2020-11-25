package com.morse.movie.ui.more.viewmodel

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.remote.datasource.MoreDataSourceManager
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movie.ui.more.entities.MoreAction
import com.morse.movie.ui.more.entities.MoreResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MoreAnnotateProcessor {

    private var moreDataStoreManager = MoreDataSourceManager()

    private val morePopularMoviesAnnotateProcessor = ObservableTransformer<MoreAction.LoadPopularMoviesByPagination , MoreResult >{
        it?.flatMap {
            Observable.create<LiveData<PagedList<Result>>> {
                try {
                 var result = moreDataStoreManager?.getPaginationMovies(0)
                 it?.onNext(result)
                }catch (e : Exception){
                    it?.onError(e)
                }
            }?.map {
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
            Observable.create<LiveData<PagedList<Result>>> {
                try {
                    var result = moreDataStoreManager?.getPaginationMovies(1)
                    it?.onNext(result)
                }catch (e : Exception){
                    it?.onError(e)
                }
            }?.map {
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
            Observable.create<LiveData<PagedList<Result>>> {
                try {
                    var result = moreDataStoreManager?.getPaginationMovies(2)
                    it?.onNext(result)
                }catch (e : Exception){
                    it?.onError(e)
                }
            }?.map {
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
            Observable.create<LiveData<PagedList<Result>>> {
                try {
                    var result = moreDataStoreManager?.getPaginationMovies(3)
                    it?.onNext(result)
                }catch (e : Exception){
                    it?.onError(e)
                }
            }?.map {
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
            )
        }
    }



}