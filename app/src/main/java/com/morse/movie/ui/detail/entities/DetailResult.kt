package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviResult
import com.morse.movie.remote.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.remote.entity.movieresponse.MovieResponse

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

}