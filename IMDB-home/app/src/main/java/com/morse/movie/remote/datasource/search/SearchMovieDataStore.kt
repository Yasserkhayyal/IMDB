package com.morse.movie.remote.datasource.search

import androidx.paging.PageKeyedDataSource
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.remote.retrofit_core.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchMovieDataStore (private var searchText : String) : PageKeyedDataSource<Int, Result>() {

    var popularPage = 1
    var ourSearchText = searchText

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

                var popularMovies = RetrofitBuilder?.getNetworkInteractor()
                    ?.searchMovie(page_id = popularPage , ourSearchText)?.await()
                block(popularMovies)

        }
    }
}