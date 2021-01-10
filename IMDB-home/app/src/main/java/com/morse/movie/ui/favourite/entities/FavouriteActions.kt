package com.morse.movie.ui.favourite.entities

import com.morse.movie.base.MviAction
import com.morse.movie.data.entity.movieresponse.Result

sealed class FavouriteActions : MviAction {

    object RemoveAllMoviesListAction  : FavouriteActions()

    object LoadFavouriteMoviesListAction  : FavouriteActions()

    data class ChechIfMovieAlreadyExistAction (public val movieId: Int) : FavouriteActions()


}