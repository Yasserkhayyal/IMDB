package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.domain.repository.DataRepository
import com.morse.movie.remote.retrofit_core.RetrofitBuilder
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LoadSimilarMovies (private var repository : DataRepository) {

    public fun execute (movieId : Int) : Observable<MovieResponse> {
        return repository?.loadSimilarMovies(movieId)
    }

}