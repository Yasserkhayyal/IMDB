package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviIntent

sealed class DetailIntent  : MviIntent{

    public data class LoadMovieDetails (var movieId : Int) : DetailIntent()

    public data class LoadSimilarMovies (var movieId : Int) : DetailIntent()

}