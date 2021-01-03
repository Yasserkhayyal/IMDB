package com.morse.movie.ui.more.entities

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.base.MviState
import com.morse.movie.data.entity.movieresponse.Result

data class MoreState (
    public var isLoading :Boolean ?= false ,
    public var error : String ?= null ,
    public var data : LiveData<PagedList<Result>> ? = null
) : MviState