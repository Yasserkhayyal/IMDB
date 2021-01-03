package com.morse.movie.remote.base

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result

interface DataSourceManager {

    fun getPaginationMovies( position : Int ): LiveData<PagedList<Result>>

    fun getPaginationMoviesSearch( movieName : String ): LiveData<PagedList<Result>>

}