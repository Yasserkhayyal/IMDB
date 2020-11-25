package com.morse.movie.ui.more.viewmodel

import androidx.lifecycle.ViewModel
import com.morse.movie.base.MviViewModel
import com.morse.movie.ui.more.entities.MoreAction
import com.morse.movie.ui.more.entities.MoreIntent
import com.morse.movie.ui.more.entities.MoreResult
import com.morse.movie.ui.more.entities.MoreState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.function.BiFunction

class MoreViewModel : ViewModel(), MviViewModel<MoreIntent, MoreState> {

    private var moreIntentCollectorSubject: PublishSubject<MoreIntent> = PublishSubject.create()
    private var moreAnnotateProcessor = MoreAnnotateProcessor()
    private var moreStateSubject: Observable<MoreState> = handleIntentAndGetStatus()

    private fun handleIntentAndGetStatus(): Observable<MoreState> {
        return moreIntentCollectorSubject
            ?.map { convertIntentToActions(it) }
            ?.compose(moreAnnotateProcessor?.moreAnnotateProcessor)
            ?.scan(MoreState(), moreReducer)
            ?.distinctUntilChanged()
            ?.replay(1)
            ?.autoConnect(0)!!

    }

    companion object {
        val moreReducer = io.reactivex.functions.BiFunction { oldState: MoreState, newResult: MoreResult ->
            return@BiFunction when (newResult) {
                is MoreResult.Loading -> {
                    return@BiFunction oldState?.copy(isLoading = true, null, null)
                }
                is MoreResult.Error -> {
                    return@BiFunction  oldState?.copy(isLoading = false, error = newResult?.e?.localizedMessage, data = null)
                }
                is MoreResult.Success -> {
                    return@BiFunction  oldState?.copy(isLoading = true, error = null,data = newResult?.data)
                }
            }
        }
    }

    private fun convertIntentToActions(intent: MoreIntent): MoreAction {

        return when (intent) {
            is MoreIntent.LoadInComingMoviesByPagination -> {
                MoreAction.LoadInComingMoviesByPagination
            }
            is MoreIntent.LoadNowPlayingMoviesByPagination -> {
                MoreAction.LoadNowPlayingMoviesByPagination
            }
            is MoreIntent.LoadPopularMoviesByPagination -> {
                MoreAction.LoadPopularMoviesByPagination
            }
            is MoreIntent.LoadTopRatedMoviesByPagination -> {
                MoreAction.LoadTopRatedMoviesByPagination
            }
        }

    }

    override fun processIntents(listOfIntents: Observable<MoreIntent>) {
        listOfIntents?.subscribe(moreIntentCollectorSubject)
    }

    override fun getStatus(): Observable<MoreState> {
        return moreStateSubject
    }
}