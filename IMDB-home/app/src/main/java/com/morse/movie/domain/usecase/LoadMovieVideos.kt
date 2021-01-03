package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.domain.repository.DataRepository
import com.morse.movie.remote.retrofit_core.RetrofitBuilder
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LoadMovieVideos ( public var repository: DataRepository) {

    public fun execute (movieId : Int) : Observable<MovieVideoResponse> {
        return repository?.loadVideoMovies(movieId)
    }

}