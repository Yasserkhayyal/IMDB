package com.morse.movie.ui.favourite.entities

import com.morse.movie.base.MviState
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result


data class FavouriteStatus(

    public var isSelectLoading : Boolean ?= false,
    public var isDeleteLoading : Boolean ?= false,

    public var selectAllMoviesError : Throwable ? = null,
    public var deleteAllMoviesError : Throwable ? = null,

    public var selectedData : ArrayList<MovieDetailResponse>?= null  ,
    public var isDeletedFinished : Boolean ?= null,

) : MviState
