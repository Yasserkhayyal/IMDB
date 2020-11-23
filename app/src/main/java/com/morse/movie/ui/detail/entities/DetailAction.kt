package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviAction
import com.morse.movie.base.MviIntent

sealed class DetailAction  : MviAction {

    public data class LoadMovieDetails (var movieId : Int) : DetailAction()

    public data class LoadSimilarMovies (var movieId : Int) : DetailAction()

}