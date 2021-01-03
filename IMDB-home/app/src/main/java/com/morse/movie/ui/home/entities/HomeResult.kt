package com.morse.movie.ui.home.entities

import com.morse.movie.base.MviResult
import com.morse.movie.data.entity.movieresponse.Result

sealed class HomeResult : MviResult {

    data class IsLoading(var position : Int) : HomeResult()

    data class Success (var listOfMovies : ArrayList<Result>, var position : Int) : HomeResult()

    data class Error (var throwable: Throwable , var position : Int) : HomeResult ()

}