package com.morse.movie.remote.retrofit_core.datasource.search

import androidx.paging.PageKeyedDataSource
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.MovieResponseDeserializer
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.remote.retrofit_core.core.RetrofitBuilder
import com.morse.movie.utils.nowplayingMovies
import com.morse.movie.utils.searchMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FuelSearchMovieDataStore (private var searchText : String) : PageKeyedDataSource<Int, Result>() {

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

            Fuel.get(searchMovies , listOf( "query" to ourSearchText ,"page_id" to popularPage))?.responseObject(MovieResponseDeserializer() , object :
                Handler<MovieResponse> {
                override fun failure(error: FuelError) {

                }

                override fun success(value: MovieResponse) {
                   block(value)
                }
            })

        }
    }
}