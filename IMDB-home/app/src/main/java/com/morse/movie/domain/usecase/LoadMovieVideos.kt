package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class LoadMovieVideos ( public var repository: DataRepository) {

    public fun execute (movieId : Int) : Observable<MovieVideoResponse> {
        return repository?.loadVideoMovies(movieId)
    }

}