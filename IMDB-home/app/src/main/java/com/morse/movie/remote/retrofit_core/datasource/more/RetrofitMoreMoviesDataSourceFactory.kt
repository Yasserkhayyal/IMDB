package com.morse.movie.remote.retrofit_core.datasource.more

import androidx.paging.DataSource
import com.morse.movie.data.entity.movieresponse.Result

class RetrofitMoreMoviesDataSourceFactory(private var position : Int) : DataSource.Factory<Int , Result>() {
    override fun create(): DataSource<Int, Result> {
        return RetrofitMoreMoviesDataSource(position)
    }
}