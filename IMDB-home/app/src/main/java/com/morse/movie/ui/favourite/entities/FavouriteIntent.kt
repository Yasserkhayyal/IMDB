package com.morse.movie.ui.favourite.entities

import com.morse.movie.base.MviIntent
import com.morse.movie.data.entity.movieresponse.Result

sealed class FavouriteIntent : MviIntent {

    object LoadFavouriteMoviesListIntent  : FavouriteIntent()

    data class ChechIfMovieAlreadyExistIntent (public val movieId: Int) : FavouriteIntent()



}
