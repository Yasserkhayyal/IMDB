package com.morse.movie.ui.favourite.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.morse.movie.base.MviViewModel
import com.morse.movie.ui.favourite.entities.FavouriteActions
import com.morse.movie.ui.favourite.entities.FavouriteIntent
import com.morse.movie.ui.favourite.entities.FavouriteResult
import com.morse.movie.ui.favourite.entities.FavouriteStatus
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class FavouriteViewModel(private val favouriteAnnotateProcessor: FavouriteAnnotateProcessor) : ViewModel(),
    MviViewModel<FavouriteIntent, FavouriteStatus> {

    private var favouriteIntentHandlerPublishSubject: PublishSubject<FavouriteIntent> =
        PublishSubject.create()
    private var favouriteStatePublishSubject: Observable<FavouriteStatus> = getFavouriteStatus()

    private fun getFavouriteStatus(): Observable<FavouriteStatus> {
        return favouriteIntentHandlerPublishSubject
            ?.distinctUntilChanged()
            ?.map { convertIntentToAction(it) }
            ?.compose(favouriteAnnotateProcessor?.favouriteAnnotateProcessor)
            ?.scan( FavouriteStatus() , favouriteReducer)!!
    }

    private fun convertIntentToAction(intent: FavouriteIntent): FavouriteActions {
        return when (intent) {

            is FavouriteIntent.LoadFavouriteMoviesListIntent -> {
                return FavouriteActions.LoadFavouriteMoviesListAction
            }

            is FavouriteIntent.ChechIfMovieAlreadyExistIntent -> {
                return FavouriteActions.ChechIfMovieAlreadyExistAction(intent?.movieId)
            }

        }
    }

    override fun processIntents(listOfIntents: Observable<FavouriteIntent>) {
        listOfIntents?.subscribe(favouriteIntentHandlerPublishSubject)
    }

    override fun getStatus(): Observable<FavouriteStatus> {
        return favouriteStatePublishSubject!!
    }

    companion object {
        val favouriteReducer = BiFunction {
                oldState : FavouriteStatus, newResult : FavouriteResult ->
            return@BiFunction when (newResult){

                is FavouriteResult.LoadFavouriteMoviesResult -> {

                    when (newResult) {

                        is FavouriteResult.LoadFavouriteMoviesResult.Loading -> { oldState?.copy(isSelectLoading = true)}

                        is FavouriteResult.LoadFavouriteMoviesResult.Success -> {oldState?.copy( isSelectLoading = false , error = null , selectedData =  newResult?.data)}

                        is FavouriteResult.LoadFavouriteMoviesResult.Error -> {oldState?.copy( isSelectLoading = false , selectedData = null , error = newResult?.error )}

                    }

                }

                is FavouriteResult.MovieExistanceResult -> {

                    when (newResult) {

                        is FavouriteResult.MovieExistanceResult.Loading -> {oldState?.copy(isSelectLoading = true)}

                        is FavouriteResult.MovieExistanceResult.Success -> {oldState?.copy(isSelectLoading = false , error = null )}

                        is FavouriteResult.MovieExistanceResult.Error -> {oldState?.copy(isSelectLoading = false , error = newResult?.error )}

                    }

                }

            }
        }
    }

}