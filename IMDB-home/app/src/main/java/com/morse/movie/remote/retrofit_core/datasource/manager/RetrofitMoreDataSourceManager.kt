package com.morse.movie.remote.retrofit_core.datasource.manager

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.remote.base.DataSourceManager
import com.morse.movie.remote.retrofit_core.datasource.more.FuelMoreMoviesDataSourceFactory
import com.morse.movie.remote.retrofit_core.datasource.search.FuelSearchMoviesDataStoreFactory

class RetrofitMoreDataSourceManager  : DataSourceManager {

    var config = PagedList.Config.Builder().setEnablePlaceholders(false)?.setPageSize(20)?.build()

   override  fun getPaginationMovies( position : Int ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(FuelMoreMoviesDataSourceFactory(position), config)?.build()
    }

    override  fun getPaginationMoviesSearch( movieName : String ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(FuelSearchMoviesDataStoreFactory( movieName), config)?.build()
    }

}