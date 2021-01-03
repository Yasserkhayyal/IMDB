package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class LoadMovieDetails ( public var repository: DataRepository) {

    public fun execute (movieId : Int) : Observable<MovieDetailResponse> {
        return repository?.loadMovieDetail(movieId)
    }

}