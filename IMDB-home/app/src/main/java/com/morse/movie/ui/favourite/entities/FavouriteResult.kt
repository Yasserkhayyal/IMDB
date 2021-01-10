package com.morse.movie.ui.favourite.entities

import com.morse.movie.base.MviResult
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result

sealed class FavouriteResult : MviResult {

    sealed class LoadFavouriteMoviesResult : FavouriteResult () {

        object Loading : LoadFavouriteMoviesResult()

        data class Error ( public val error : Throwable) : LoadFavouriteMoviesResult()

        data class Success ( public val data : ArrayList<MovieDetailResponse>) : LoadFavouriteMoviesResult()

    }

    sealed class MovieExistanceResult : FavouriteResult () {

        object Loading : MovieExistanceResult()

        data class Error ( public val error : Throwable) : MovieExistanceResult()

        data class Success ( public val data : Boolean) : MovieExistanceResult()

    }

    sealed class RemoveAllMoviesResult : FavouriteResult () {

        object Loading : RemoveAllMoviesResult()

        data class Error ( public val error : Throwable) : RemoveAllMoviesResult()

        data class Success ( public val data : Boolean) : RemoveAllMoviesResult()

    }



}
