package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class LoadMovieReviews ( public var repository: DataRepository){

    public fun execute (movieId : Int) : Observable<MovieReview> {
        return repository?.loadReviewMovies(movieId)
    }

}