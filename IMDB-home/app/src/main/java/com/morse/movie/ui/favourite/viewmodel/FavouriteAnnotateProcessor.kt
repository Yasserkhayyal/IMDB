package com.morse.movie.ui.favourite.viewmodel

import android.content.Context
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.domain.usecase.CheckIfMovieExistInDataBase
import com.morse.movie.domain.usecase.ClearAllMovies
import com.morse.movie.domain.usecase.LoadFavouriteMovies
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.ui.favourite.entities.FavouriteActions
import com.morse.movie.ui.favourite.entities.FavouriteResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteAnnotateProcessor (
    private val isMovieExistInDataBase : CheckIfMovieExistInDataBase ,
    private val loadAllMoviesFromDB : LoadFavouriteMovies ,
    private val removeAllMoviesFromDB : ClearAllMovies
) {

    private val checkIfExistInFavouriteAnnotateProcessor = ObservableTransformer<FavouriteActions.ChechIfMovieAlreadyExistAction , FavouriteResult.MovieExistanceResult>{
        it?.flatMap {
            isMovieExistInDataBase?.execute(it?.movieId)
                ?.map {
                    FavouriteResult.MovieExistanceResult.Success(it)
                }
                ?.cast(FavouriteResult.MovieExistanceResult::class.java)
                ?.onErrorReturn {
                    FavouriteResult.MovieExistanceResult.Error(it)
                }
                ?. observeOn (AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.computation())
                ?.startWith(FavouriteResult.MovieExistanceResult.Loading)
        }
    }

    private val selectAllMoviesFromFavouriteAnnotateProcessor = ObservableTransformer<FavouriteActions.LoadFavouriteMoviesListAction , FavouriteResult.LoadFavouriteMoviesResult>{
        it?.flatMap {
            loadAllMoviesFromDB?.execute()
                ?.map {
                    FavouriteResult.LoadFavouriteMoviesResult.Success(it as ArrayList<MovieDetailResponse>)
                }
                ?.cast(FavouriteResult.LoadFavouriteMoviesResult::class.java)
                ?.onErrorReturn {
                    FavouriteResult.LoadFavouriteMoviesResult.Error(it)
                }
                ?. observeOn (AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.computation())
                ?.startWith(FavouriteResult.LoadFavouriteMoviesResult.Loading)
        }
    }

    private val removeAllMoviesFromFavouriteAnnotateProcessor = ObservableTransformer<FavouriteActions.RemoveAllMoviesListAction , FavouriteResult.RemoveAllMoviesResult>{
        it?.flatMap {
            removeAllMoviesFromDB?.execute()
                ?.map {
                    FavouriteResult.RemoveAllMoviesResult.Success(true)
                }
                ?.cast(FavouriteResult.RemoveAllMoviesResult::class.java)
                ?.onErrorReturn {
                    FavouriteResult.RemoveAllMoviesResult.Error(it)
                }
                ?. observeOn (AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.computation())
                ?.startWith(FavouriteResult.RemoveAllMoviesResult.Loading)
        }
    }

    public var favouriteAnnotateProcessor = ObservableTransformer<FavouriteActions , FavouriteResult>{

        it?.publish {

            Observable.merge(
                it?.ofType(FavouriteActions.ChechIfMovieAlreadyExistAction::class.java).compose(checkIfExistInFavouriteAnnotateProcessor) ,
                it?.ofType(FavouriteActions.LoadFavouriteMoviesListAction::class.java).compose(selectAllMoviesFromFavouriteAnnotateProcessor) ,
                it?.ofType(FavouriteActions.RemoveAllMoviesListAction::class.java).compose(removeAllMoviesFromFavouriteAnnotateProcessor)
            )

        }

    }

}