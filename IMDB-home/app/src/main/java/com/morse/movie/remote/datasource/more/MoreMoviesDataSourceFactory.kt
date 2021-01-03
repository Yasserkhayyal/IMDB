package com.morse.movie.remote.datasource.more

import androidx.paging.DataSource
import com.morse.movie.data.entity.movieresponse.Result

class MoreMoviesDataSourceFactory(private var position : Int) : DataSource.Factory<Int , Result>() {
    override fun create(): DataSource<Int, Result> {
        return MoreMoviesDataSource(position)
    }
}