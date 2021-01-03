package com.morse.movie.ui.detail.viewmodel

import android.content.Context
import com.morse.movie.remote.retrofit_core.RetrofitBuilder
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.domain.usecase.*
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.ui.detail.entities.DetailAction
import com.morse.movie.ui.detail.entities.DetailResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailAnnotateProcessor(
    private val loadMovieDetails: LoadMovieDetails,
    private val loadMovieSimilars: LoadSimilarMovies ,
    private val loadMovieReviews: LoadMovieReviews ,
    private val loadMovieVideos: LoadMovieVideos ,
    private val addMovieToFavourites: AddMovieToFavourites ,
    private val removeMovieFromFavourites: RemoveMovieFromFavourites ,
    private val isMovieExistInDataBase : CheckIfMovieExistInDataBase
) {


    private val loadMovieDetailAnnotateProcessor =
        ObservableTransformer<DetailAction.LoadMovieDetails, DetailResult.MovieDetailResult> {
            it?.flatMap {
                return@flatMap loadMovieDetails?.execute(it?.movieId)
                    ?.map {
                        DetailResult.MovieDetailResult.Success(it)
                    }?.cast(DetailResult.MovieDetailResult::class.java)
                    ?.onErrorReturn { DetailResult.MovieDetailResult.Error(it) }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.MovieDetailResult.Loading)
            }

        }

    private val loadSimilarMoviesAnnotateProcessor =
        ObservableTransformer<DetailAction.LoadSimilarMovies, DetailResult.MovieSimilarsResult> {
            it?.flatMap {
                return@flatMap loadMovieSimilars?.execute(it?.movieId)

                    ?.map {
                        DetailResult.MovieSimilarsResult.Success(it)
                    }?.cast(DetailResult.MovieSimilarsResult::class.java)
                    ?.onErrorReturn { DetailResult.MovieSimilarsResult.Error(it) }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.MovieSimilarsResult.Loading)!!
            }

        }


    private val loadMovieReviewAnnotateProcessor =
        ObservableTransformer<DetailAction.LoadMovieReviewsAction, DetailResult.MovieReviewsResult> {
            it?.flatMap {
                return@flatMap loadMovieReviews?.execute(it?.movieId)
                    ?.map {
                        DetailResult.MovieReviewsResult.Success(it)
                    }?.cast(DetailResult.MovieReviewsResult::class.java)
                    ?.onErrorReturn { DetailResult.MovieReviewsResult.Error(it) }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.MovieReviewsResult.Loading)!!
            }

        }

    private val loadMovieVideosAnnotateProcessor =
        ObservableTransformer<DetailAction.LoadMovieVideosAction, DetailResult.MovieVideosResult> {
            it?.flatMap {
                return@flatMap loadMovieVideos?.execute(it?.movieId)
                    ?.map {
                        DetailResult.MovieVideosResult.Success(it)
                    }
                    ?.cast(DetailResult.MovieVideosResult::class.java)
                    ?.onErrorReturn { DetailResult.MovieVideosResult.Error(it) }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.MovieVideosResult.Loading)
            }

        }

    private val addToFavouriteAnnotateProcessor =
        ObservableTransformer<DetailAction.AddMovieToFavouriteAction, DetailResult.AddMovieResult> {

            it?.flatMap {
                addMovieToFavourites?.execute(it?.movie)
                    ?.map {
                        DetailResult.AddMovieResult.Success(it)
                    }
                    ?.cast(DetailResult.AddMovieResult::class.java)
                    ?.onErrorReturn {
                        DetailResult.AddMovieResult.Error(it)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.AddMovieResult.Loading)
            }

        }

    private val removeFromFavouriteAnnotateProcessor =
        ObservableTransformer<DetailAction.RemoveMovieFromFavouriteAction, DetailResult.RemoveMovieResult> {

            it?.flatMap {
                removeMovieFromFavourites?.execute(it?.movieId)
                    ?.map {
                        DetailResult.RemoveMovieResult.Success(it)
                    }
                    ?.cast(DetailResult.RemoveMovieResult::class.java)
                    ?.onErrorReturn {
                        DetailResult.RemoveMovieResult.Error(it)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.RemoveMovieResult.Loading)
            }

        }

    private val isExistInFavouriteAnnotateProcessor =
        ObservableTransformer<DetailAction.IsMovieExistInDatabaseAction, DetailResult.IsMovieExistInFavouriteResult> {

            it?.flatMap {
                isMovieExistInDataBase?.execute(it?.movieId)
                    ?.map {
                        DetailResult.IsMovieExistInFavouriteResult.Success(it)
                    }
                    ?.cast(DetailResult.IsMovieExistInFavouriteResult::class.java)
                    ?.onErrorReturn {
                        DetailResult.IsMovieExistInFavouriteResult.Error(it)
                    }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.computation())
                    ?.startWith(DetailResult.IsMovieExistInFavouriteResult.Loading)
            }

        }

    public val detailAnnotateProcesser = ObservableTransformer<DetailAction, DetailResult> {
        it?.publish {
            Observable.merge(
                it?.ofType(DetailAction.LoadMovieDetails::class.java)
                    .compose(loadMovieDetailAnnotateProcessor),
                it?.ofType(DetailAction.LoadSimilarMovies::class.java)
                    .compose(loadSimilarMoviesAnnotateProcessor),
                it?.ofType(DetailAction.AddMovieToFavouriteAction::class.java)
                    .compose(addToFavouriteAnnotateProcessor),
                it?.ofType(DetailAction.RemoveMovieFromFavouriteAction::class.java)
                    .compose(removeFromFavouriteAnnotateProcessor)
            )?.mergeWith(
                it?.ofType(DetailAction.IsMovieExistInDatabaseAction::class.java)
                    .compose(isExistInFavouriteAnnotateProcessor)
            )?.mergeWith(
                it?.ofType(DetailAction.LoadMovieReviewsAction::class.java)
                    .compose(loadMovieReviewAnnotateProcessor)
            )?.mergeWith(
                it?.ofType(DetailAction.LoadMovieVideosAction::class.java)
                    .compose(loadMovieVideosAnnotateProcessor)
            )
        }
    }


}