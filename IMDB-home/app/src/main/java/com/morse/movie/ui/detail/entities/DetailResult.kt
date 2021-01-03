package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviResult
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.ui.favourite.entities.FavouriteResult

sealed class DetailResult : MviResult {

    sealed class MovieDetailResult : DetailResult(){

        data class Success (var movieDetailResponse: MovieDetailResponse) : MovieDetailResult()

        data class Error (var error : Throwable) : MovieDetailResult()

        object Loading : MovieDetailResult()

    }

    sealed class MovieSimilarsResult : DetailResult(){

        data class Success (var movieDetailResponse: MovieResponse) : MovieSimilarsResult()

        data class Error (var error : Throwable) : MovieSimilarsResult()

        object Loading : MovieSimilarsResult()

    }

    sealed class AddMovieResult : DetailResult() {

        object Loading : AddMovieResult()

        data class Error ( public val error : Throwable) : AddMovieResult()

        data class Success ( public val data : Boolean) : AddMovieResult()

    }

    sealed class RemoveMovieResult : DetailResult() {

        object Loading : RemoveMovieResult()

        data class Error ( public val error : Throwable) : RemoveMovieResult()

        data class Success ( public val data : Boolean) : RemoveMovieResult()

    }

    sealed class IsMovieExistInFavouriteResult : DetailResult() {

        object Loading : IsMovieExistInFavouriteResult()

        data class Error ( public val error : Throwable) : IsMovieExistInFavouriteResult()

        data class Success ( public val data : Boolean) : IsMovieExistInFavouriteResult()

    }

    sealed class MovieReviewsResult : DetailResult() {

        object Loading : MovieReviewsResult()

        data class Error ( public val error : Throwable) : MovieReviewsResult()

        data class Success ( public val data : MovieReview) : MovieReviewsResult()
    }

    sealed class MovieVideosResult : DetailResult() {

        object Loading : MovieVideosResult()

        data class Error ( public val error : Throwable) : MovieVideosResult()

        data class Success ( public val data : MovieVideoResponse) : MovieVideosResult()
    }

}