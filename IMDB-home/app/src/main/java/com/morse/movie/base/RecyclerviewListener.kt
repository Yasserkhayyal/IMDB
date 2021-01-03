package com.morse.movie.base

import android.view.View
import com.morse.movie.data.entity.movieresponse.Result

interface RecyclerviewListener <in I> {

    public fun onItemPressed (card : View? =null, data: I?=null, color : Int ?= null)

}