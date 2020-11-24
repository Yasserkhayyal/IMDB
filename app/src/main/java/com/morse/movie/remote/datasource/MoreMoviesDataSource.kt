package com.morse.movies.movies.data.datasource.populardatasource

import androidx.paging.PageKeyedDataSource
import com.morse.movie.remote.core.RetrofitBuilder
import com.morse.movie.remote.entity.movieresponse.MovieResponse
import com.morse.movie.remote.entity.movieresponse.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoreMoviesDataSource (): PageKeyedDataSource<Int, Result>() {
    var popularPage = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Result>
    ) {
       executeMovieQuery {
           popularPage ++
           callback?.onResult(it?.results!! , null , popularPage)
       }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        popularPage = params?.key
        if(params?.key <= 500) {
            executeMovieQuery {
                callback?.onResult(it?.results!!, popularPage++)
            }
            popularPage ++
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {

    }
    private fun executeMovieQuery (block : (MovieResponse)->Unit) {

        CoroutineScope(Dispatchers.IO)?.launch {
            var popularMovies = RetrofitBuilder?.getNetworkInteractor()?.getPopularMovie()?.await()
            block(popularMovies)
        }
    }
}