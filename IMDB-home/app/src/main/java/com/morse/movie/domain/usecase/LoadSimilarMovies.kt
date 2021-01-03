package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class LoadSimilarMovies (private var repository : DataRepository) {

    public fun execute (movieId : Int) : Observable<MovieResponse> {
        return repository?.loadSimilarMovies(movieId)
    }

}