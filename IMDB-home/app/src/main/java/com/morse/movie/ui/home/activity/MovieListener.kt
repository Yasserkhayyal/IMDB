package com.morse.movie.ui.home.activity

import android.view.View
import com.morse.movie.data.entity.movieresponse.Result

interface MovieListener {

    public fun onMovieClicks ( movieCard : View , movieResult: Result , color : Int ?= null)

}