package com.morse.movie.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.morse.movie.base.MviViewModel
import com.morse.movie.ui.detail.entities.DetailAction
import com.morse.movie.ui.detail.entities.DetailIntent
import com.morse.movie.ui.detail.entities.DetailResult
import com.morse.movie.ui.detail.entities.DetailState
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class DetailViewModel : ViewModel(), MviViewModel<DetailIntent, DetailState> {

    private var detailIntentHandler: PublishSubject<DetailIntent> = PublishSubject.create()
    private var detailAnnotateProcessor = DetailAnnotateProcessor()
    private var detailStateHandler = handleIntentsAndReturnStatus()


    override fun processIntents(listOfIntents: Observable<DetailIntent>) {
        listOfIntents?.subscribe(detailIntentHandler)
    }

    override fun getStatus(): Observable<DetailState> {
        return detailIntentHandler
            ?.map { convertOurIntentToActions(it) }
            ?.compose(detailAnnotateProcessor?.detailAnnotateProcesser)
            ?.scan(DetailState(),detailReducer)
            ?.distinctUntilChanged()
            ?.replay(1)
            ?.autoConnect(0)!!
    }

    private fun handleIntentsAndReturnStatus(): Observable<DetailState> {
        return detailStateHandler
    }

    private fun convertOurIntentToActions(intent: DetailIntent): DetailAction {
        return when (intent) {
            is DetailIntent.LoadMovieDetails -> {
                DetailAction.LoadMovieDetails(intent?.movieId)
            }
            is DetailIntent.LoadSimilarMovies -> {
                DetailAction.LoadSimilarMovies(intent?.movieId)
            }
        }
    }

    companion object {
        val detailReducer = BiFunction {
            oldState : DetailState , newResult : DetailResult ->
            return@BiFunction when (newResult){

                is DetailResult.MovieDetailResult -> {

                    when (newResult) {

                        is DetailResult.MovieDetailResult.Loading -> { oldState?.copy(isLoadingForDetail = true) }

                        is DetailResult.MovieDetailResult.Success -> {oldState?.copy(isLoadingForDetail = false , movieDetail = newResult?.movieDetailResponse)}

                        is DetailResult.MovieDetailResult.Error -> {oldState?.copy(isLoadingForDetail = false , errorDetail = newResult?.error?.localizedMessage)}

                    }

                }

                is DetailResult.MovieSimilarsResult -> {

                    when (newResult) {

                        is DetailResult.MovieSimilarsResult.Loading -> {oldState?.copy(isLoadingForSimilar = true)}

                        is DetailResult.MovieSimilarsResult.Success -> {oldState?.copy(isLoadingForSimilar = false , similarMovies = newResult?.movieDetailResponse)}

                        is DetailResult.MovieSimilarsResult.Error -> {oldState?.copy(isLoadingForSimilar = false , errorDetail = newResult?.error?.localizedMessage)}

                    }

                }

            }
        }
    }

}