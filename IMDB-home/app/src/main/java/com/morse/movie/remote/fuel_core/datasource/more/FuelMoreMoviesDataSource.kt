package com.morse.movie.remote.retrofit_core.datasource.more

import androidx.paging.PageKeyedDataSource
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Handler
import com.morse.movie.remote.retrofit_core.core.RetrofitBuilder
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.MovieResponseDeserializer
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FuelMoreMoviesDataSource (private var position : Int): PageKeyedDataSource<Int, Result>() {
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
        try {
            CoroutineScope(Dispatchers.IO)?.launch {

                if (position == 0) {

                    Fuel.get(popularMovies , listOf("page_id" to popularPage))?.responseObject(
                        MovieResponseDeserializer() , object :
                            Handler<MovieResponse> {
                        override fun failure(error: FuelError) {

                        }

                        override fun success(value: MovieResponse) {
                            block(value)
                        }
                    })

                }

                else if (position == 1) {

                    Fuel.get(topRatedMovies , listOf("page_id" to popularPage))?.responseObject(
                        MovieResponseDeserializer() , object :
                            Handler<MovieResponse> {
                            override fun failure(error: FuelError) {

                            }

                            override fun success(value: MovieResponse) {
                                block(value)
                            }
                        })


                }

                else if (position == 2) {

                    Fuel.get(inCommingMovies , listOf("page_id" to popularPage))?.responseObject(
                        MovieResponseDeserializer() , object :
                            Handler<MovieResponse> {
                            override fun failure(error: FuelError) {

                            }

                            override fun success(value: MovieResponse) {
                                block(value)
                            }
                        })

                }

                else if (position == 3) {

                    Fuel.get(nowplayingMovies , listOf("page_id" to popularPage))?.responseObject(
                        MovieResponseDeserializer() , object :
                            Handler<MovieResponse> {
                            override fun failure(error: FuelError) {

                            }

                            override fun success(value: MovieResponse) {
                                block(value)
                            }
                        })

                }

            }
        }catch (e : Exception) {

        }
    }
}