package com.morse.movie.remote.datasource

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movies.movies.data.datasource.populardatasource.MoreMoviesDataSourceFactory

class MoreDataSourceManager {

    var config = PagedList.Config.Builder().setEnablePlaceholders(false)?.setPageSize(20)?.build()

    fun getPaginationMovies( position : Int ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(MoreMoviesDataSourceFactory(position), config)?.build()
    }

}