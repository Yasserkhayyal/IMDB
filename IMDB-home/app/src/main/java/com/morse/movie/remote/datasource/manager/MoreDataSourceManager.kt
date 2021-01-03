package com.morse.movie.remote.datasource.manager

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.remote.datasource.more.MoreMoviesDataSourceFactory
import com.morse.movie.remote.datasource.search.SearchMovieDataStore
import com.morse.movie.remote.datasource.search.SearchMoviesDataStoreFactory

class MoreDataSourceManager {

    var config = PagedList.Config.Builder().setEnablePlaceholders(false)?.setPageSize(20)?.build()

    fun getPaginationMovies( position : Int ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(MoreMoviesDataSourceFactory(position), config)?.build()
    }

    fun getPaginationMoviesSearch( movieName : String ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(SearchMoviesDataStoreFactory( movieName), config)?.build()
    }

}