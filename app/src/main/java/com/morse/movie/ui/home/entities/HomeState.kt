package com.morse.movie.ui.home.entities

import com.morse.movie.base.MviState
import com.morse.movie.remote.entity.Result

data class HomeState (
    var isLoading : Boolean ?= false ,
    var result: ArrayList<Result> ? = null ,
    var error : String ?= null ,
    var position : Int ?= 0
): MviState