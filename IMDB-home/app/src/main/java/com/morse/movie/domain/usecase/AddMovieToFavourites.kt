package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.domain.repository.DataRepository

class AddMovieToFavourites ( public var repository: DataRepository) {

    public fun execute ( movie : MovieDetailResponse ) = repository?.addMovieToFavourite(movie)

}