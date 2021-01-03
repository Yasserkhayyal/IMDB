package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class LoadTopRatedMovies (private var repository : DataRepository) {

    public fun execute () : Observable<MovieResponse> {
        return repository?.loadTopRatedMovies()
    }

}