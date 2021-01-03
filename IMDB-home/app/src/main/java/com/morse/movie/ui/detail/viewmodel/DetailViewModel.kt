package com.morse.movie.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.morse.movie.base.MviViewModel
import com.morse.movie.ui.detail.entities.DetailAction
import com.morse.movie.ui.detail.entities.DetailIntent
import com.morse.movie.ui.detail.entities.DetailResult
import com.morse.movie.ui.detail.entities.DetailState
import com.morse.movie.ui.favourite.entities.FavouriteActions
import com.morse.movie.ui.favourite.entities.FavouriteIntent
import com.morse.movie.ui.favourite.entities.FavouriteResult
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class DetailViewModel (private val detailAnnotateProcessor: DetailAnnotateProcessor) : ViewModel(), MviViewModel<DetailIntent, DetailState> {

    private var detailIntentHandler: PublishSubject<DetailIntent> = PublishSubject.create()
    private var detailStateHandler = handleIntentsAndReturnStatus()


    override fun processIntents(listOfIntents: Observable<DetailIntent>) {
        listOfIntents?.subscribe(detailIntentHandler)
    }

    override fun getStatus(): Observable<DetailState> {
        return detailIntentHandler
            ?.distinctUntilChanged()
            ?.map { convertOurIntentToActions(it) }
            ?.compose(detailAnnotateProcessor?.detailAnnotateProcesser)
            ?.scan(DetailState(),detailReducer)!!
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

            is DetailIntent.LoadMovieReviewsIntent -> {
                DetailAction.LoadMovieReviewsAction(intent?.movieId)
            }
            is DetailIntent.LoadMovieVideosIntent -> {
                DetailAction.LoadMovieVideosAction(intent?.movieId)
            }

            is DetailIntent.AddMovieToFavouriteIntent -> {
                return DetailAction.AddMovieToFavouriteAction(intent?.movie)
            }
            is DetailIntent.RemoveMovieFromFavouriteIntent -> {
                return DetailAction.RemoveMovieFromFavouriteAction(intent?.movieId)
            }
            is DetailIntent.IsMovieExistInDatabaseIntent -> {
                return DetailAction.IsMovieExistInDatabaseAction(intent?.movieId)
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

                        is DetailResult.MovieDetailResult.Success -> {oldState?.copy(isLoadingForDetail = false , movieDetail = newResult?.movieDetailResponse , isExist = null)}

                        is DetailResult.MovieDetailResult.Error -> {oldState?.copy(isLoadingForDetail = false , errorDetail = newResult?.error?.localizedMessage , isExist = null)}

                    }

                }

                is DetailResult.MovieSimilarsResult -> {

                    when (newResult) {

                        is DetailResult.MovieSimilarsResult.Loading -> {oldState?.copy(isLoadingForSimilar = true)}

                        is DetailResult.MovieSimilarsResult.Success -> {oldState?.copy(isLoadingForSimilar = false , similarMovies = newResult?.movieDetailResponse, isExist = null)}

                        is DetailResult.MovieSimilarsResult.Error -> {oldState?.copy(isLoadingForSimilar = false , errorDetail = newResult?.error?.localizedMessage, isExist = null)}

                    }

                }

                is DetailResult.MovieReviewsResult -> {

                    when (newResult) {

                        is DetailResult.MovieReviewsResult.Loading -> { oldState?.copy(isLoadingReviews  = true) }

                        is DetailResult.MovieReviewsResult.Success -> {oldState?.copy(isLoadingReviews = false , movieReviews = newResult?.data , isExist = null)}

                        is DetailResult.MovieReviewsResult.Error -> {oldState?.copy(isLoadingReviews = false , errorReviews = newResult?.error?.localizedMessage , isExist = null)}

                    }

                }

                is DetailResult.MovieVideosResult -> {

                    when (newResult) {

                        is DetailResult.MovieVideosResult.Loading -> { oldState?.copy(isLoadingVideos = true) }

                        is DetailResult.MovieVideosResult.Success -> {oldState?.copy(isLoadingVideos = false , movieVideos = newResult?.data , isExist = null)}

                        is DetailResult.MovieVideosResult.Error -> {oldState?.copy(isLoadingVideos = false , errorVideos = newResult?.error?.localizedMessage , isExist = null)}

                    }

                }

                is DetailResult.RemoveMovieResult -> {

                    when (newResult) {

                        is DetailResult.RemoveMovieResult.Loading -> {oldState?.copy(isRemoveLoading = true)}

                        is DetailResult.RemoveMovieResult.Success -> {oldState?.copy(isRemoveLoading = false  , errorDetail = null , isRemoved = newResult?.data , isExist = null )}

                        is DetailResult.RemoveMovieResult.Error -> {oldState?.copy(isRemoveLoading = false , errorDetail = newResult?.error?.localizedMessage , isRemoved = null , isExist = null)}

                    }

                }

                is DetailResult.AddMovieResult -> {

                    when (newResult) {

                        is DetailResult.AddMovieResult.Loading -> {oldState?.copy(isAddLoading = true)}

                        is DetailResult.AddMovieResult.Success -> {oldState?.copy(isAddLoading = false , errorDetail = null , isAdded = newResult?.data , isExist = null)}

                        is DetailResult.AddMovieResult.Error -> {oldState?.copy(isAddLoading = false , errorDetail = newResult?.error?.localizedMessage ,isAdded = null , isExist = null)}

                    }

                }

                is DetailResult.IsMovieExistInFavouriteResult -> {

                    when (newResult) {

                        is DetailResult.IsMovieExistInFavouriteResult.Loading -> {oldState?.copy(isExistInFavouriteLoading = true)}

                        is DetailResult.IsMovieExistInFavouriteResult.Success -> {oldState?.copy(isExistInFavouriteLoading = false , errorExistInFavourite = null , isExist = newResult?.data)}

                        is DetailResult.IsMovieExistInFavouriteResult.Error -> {oldState?.copy(isExistInFavouriteLoading = false , errorExistInFavourite = newResult?.error?.localizedMessage ,isExist = null)}

                    }

                }
            }
        }
    }

}