package com.morse.movies.movies.data.datasource.populardatasource

import androidx.paging.DataSource
import com.morse.movie.remote.entity.movieresponse.Result

class MoreMoviesDataSourceFactory : DataSource.Factory<Int , Result>() {
    override fun create(): DataSource<Int, Result> {
        return MoreMoviesDataSource()
    }
}