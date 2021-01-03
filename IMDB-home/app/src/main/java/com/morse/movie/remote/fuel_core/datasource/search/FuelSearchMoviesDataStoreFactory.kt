package com.morse.movie.remote.retrofit_core.datasource.search

import androidx.paging.DataSource
import com.morse.movie.data.entity.movieresponse.Result

class FuelSearchMoviesDataStoreFactory (private var searchFilter : String) : DataSource.Factory<Int , Result>(){
    override fun create(): DataSource<Int, Result> {
        return FuelSearchMovieDataStore(searchFilter)
    }
}