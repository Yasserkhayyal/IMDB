package com.morse.movie.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.morse.movie.base.MviViewModel
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.ui.home.entities.HomeAction
import com.morse.movie.ui.home.entities.HomeIntent
import com.morse.movie.ui.home.entities.HomeResult
import com.morse.movie.ui.home.entities.HomeState
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class HomeViewModel ( private val homeAnnotateProcessor : HomeAnnotateProcessor) : ViewModel(), MviViewModel<HomeIntent, HomeState> {



    private var intents: PublishSubject<HomeIntent> = PublishSubject.create()
    //private var homeAnnotateProcessor : HomeAnnotateProcessor = HomeAnnotateProcessor()
    private var states: Observable<HomeState> = getStatesObservableObject()

    override fun processIntents(listOfIntents: Observable<HomeIntent>) {
        listOfIntents?.subscribe(intents)
    }

    override fun getStatus(): Observable<HomeState> {
        return states
    }

    private fun getStatesObservableObject(): Observable<HomeState> {
        return intents
            ?.distinctUntilChanged()
            ?.map { convertIntentToActions(it) }
            ?.compose(homeAnnotateProcessor?.homeViewModelAnnotateProcessor)
            ?.scan(HomeState() , resultMaker)!!
    }

    companion object {
        val resultMaker = BiFunction {  oldState: HomeState ,newOutput: HomeResult ->
            return@BiFunction when (newOutput){
                is HomeResult.IsLoading -> {
                   return@BiFunction oldState?.copy(true , null , null , newOutput?.position)
                }
                is HomeResult.Success -> {
                    return@BiFunction oldState?.copy(false , newOutput?.listOfMovies , null , newOutput?.position)
                }
                is HomeResult.Error -> {
                    return@BiFunction oldState?.copy(false , null , newOutput?.throwable?.localizedMessage , newOutput?.position)
                }
            }

        }
    }

    private fun convertIntentToActions(intent: HomeIntent): HomeAction {
        return when (intent) {
            is HomeIntent.LoadPopularMovies -> {
                return HomeAction.LoadPopularMovies
            }
            is HomeIntent.LoadTopRatedMovies -> {
                return HomeAction.LoadTopRatedMovies
            }
            is HomeIntent.LoadIncomingMovies -> {
                return HomeAction.LoadIncomingMovies
            }
            is HomeIntent.LoadNowPlayingMovies -> {
                return HomeAction.LoadNowPlayingMovies
            }
        }
    }
}