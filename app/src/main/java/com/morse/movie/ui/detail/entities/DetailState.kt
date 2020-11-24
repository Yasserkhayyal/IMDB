package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviState
import com.morse.movie.remote.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.remote.entity.movieresponse.MovieResponse

data class DetailState (
    var isLoadingForDetail : Boolean?= false,
    var isLoadingForSimilar : Boolean? = false,
    var errorDetail : String ?= null,
    var errorSimilar : String ?= null,
    var movieDetail : MovieDetailResponse ? = null,
    var similarMovies : MovieResponse ?= null
) : MviState