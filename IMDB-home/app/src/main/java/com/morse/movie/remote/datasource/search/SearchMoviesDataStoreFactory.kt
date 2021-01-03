package com.morse.movie.remote.datasource.search

import androidx.paging.DataSource
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.remote.datasource.more.MoreMoviesDataSource

class SearchMoviesDataStoreFactory (private var searchFilter : String) : DataSource.Factory<Int , Result>(){
    override fun create(): DataSource<Int, Result> {
        return SearchMovieDataStore(searchFilter)
    }
}