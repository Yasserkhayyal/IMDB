package com.morse.movie.remote.datasource.more

import androidx.paging.PageKeyedDataSource
import com.morse.movie.remote.retrofit_core.RetrofitBuilder
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MoreMoviesDataSource (private var position : Int): PageKeyedDataSource<Int, Result>() {
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
                    var popularMovies = RetrofitBuilder?.getNetworkInteractor()
                        ?.getPopularMovieWithPagination(page_id = popularPage)?.await()
                    block(popularMovies)
                } else if (position == 1) {
                    var popularMovies = RetrofitBuilder?.getNetworkInteractor()
                        ?.getTopRatedMovieWithPagination(page_id = popularPage)?.await()
                    block(popularMovies)
                } else if (position == 2) {
                    var popularMovies = RetrofitBuilder?.getNetworkInteractor()
                        ?.getIncomingMovieWithPagination(page_id = popularPage)?.await()
                    block(popularMovies)
                } else if (position == 3) {
                    var popularMovies = RetrofitBuilder?.getNetworkInteractor()
                        ?.getNowPlayingMovieWithPagination(page_id = popularPage)?.await()
                    block(popularMovies)
                }
            }
        }catch (e : Exception) {

        }
    }
}