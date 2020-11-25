package com.morse.movie.ui.more.entities

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.base.MviResult
import com.morse.movie.remote.entity.movieresponse.Result

sealed class MoreResult : MviResult {

    object Loading : MoreResult()

    data class Error (  var e : Throwable) : MoreResult()

    data class Success (  var data : LiveData<PagedList<Result>> ) : MoreResult()

}