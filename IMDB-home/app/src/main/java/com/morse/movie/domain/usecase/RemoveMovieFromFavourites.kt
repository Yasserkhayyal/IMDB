package com.morse.movie.domain.usecase

import com.morse.movie.domain.repository.DataRepository

class RemoveMovieFromFavourites  ( private var repository : DataRepository) {

    public fun execute (movieId : Int) = repository?.removeMovieFromFavourite(movieId)

}