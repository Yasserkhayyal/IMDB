package com.morse.movie.remote.retrofit_core.datasource.manager

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.remote.base.DataSourceManager
import com.morse.movie.remote.retrofit_core.datasource.more.FuelMoreMoviesDataSourceFactory
import com.morse.movie.remote.retrofit_core.datasource.more.RetrofitMoreMoviesDataSourceFactory
import com.morse.movie.remote.retrofit_core.datasource.search.FuelSearchMoviesDataStoreFactory
import com.morse.movie.remote.retrofit_core.datasource.search.RetrofitSearchMoviesDataStoreFactory

class RetrofitMoreDataSourceManager  : DataSourceManager {

    var config = PagedList.Config.Builder().setEnablePlaceholders(false)?.setPageSize(20)?.build()

   override  fun getPaginationMovies( position : Int ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(RetrofitMoreMoviesDataSourceFactory(position), config)?.build()
    }

    override  fun getPaginationMoviesSearch( movieName : String ): LiveData<PagedList<Result>> {
        return LivePagedListBuilder<Int, Result>(RetrofitSearchMoviesDataStoreFactory( movieName), config)?.build()
    }

}