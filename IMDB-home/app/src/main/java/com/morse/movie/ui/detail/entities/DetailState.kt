package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviState
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.entity.personresponse.PersonResponse

data class DetailState(
    var isLoadingReviews : Boolean? =false ,
    var isLoadingVideos : Boolean? =false ,
    var isAddLoading: Boolean? = false,
    var isRemoveLoading: Boolean? = false,
    var isLoadingForDetail: Boolean? = false,
    var isLoadingForSimilar: Boolean? = false,
    var isExistInFavouriteLoading : Boolean?= false ,
    var isUserProfileLoading : Boolean ?=false ,

    var errorDetail: String? = null,
    var errorSimilar: String? = null,
    var errorExistInFavourite : String ?= null ,
    var errorReviews: String? = null,
    var errorVideos : String ?= null ,
    var userPorfileError : String? = null ,

    var movieDetail: MovieDetailResponse? = null,
    var similarMovies: MovieResponse? = null,
    var movieReviews: MovieReview? = null,
    var movieVideos: MovieVideoResponse? = null,
    var isAdded: Boolean? = null,
    var isRemoved: Boolean? = null,
    var isExist: Boolean? = null ,
    var userProfile : PersonResponse? =null

) : MviState