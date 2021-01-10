package com.morse.movie.domain.usecase

import com.morse.movie.domain.repository.DataRepository

class ClearAllMovies  ( public var repository: DataRepository) {

    public fun execute () = repository?.removeAllMovieFromFavourite()

}