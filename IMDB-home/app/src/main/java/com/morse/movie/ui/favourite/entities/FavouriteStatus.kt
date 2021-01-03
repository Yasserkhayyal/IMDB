package com.morse.movie.ui.favourite.entities

import com.morse.movie.base.MviState
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result


data class FavouriteStatus(
    public var isSelectLoading : Boolean ?= false,

    public var error : Throwable ? = null,

    public var selectedData : ArrayList<MovieDetailResponse>?= null
) : MviState
